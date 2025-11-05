# https://www.keycloak.org/docs-api/latest/rest-api/index.html#UserRepresentation

import json
import requests
from dataclasses import dataclass, asdict
from typing import List, Dict, Any, Optional


@dataclass
class Credentials:
    type: str
    value: str
    temporary: bool


@dataclass
class User:
    username: str
    firstName: str
    lastName: str
    email: str
    enabled: bool
    credentials: List[Credentials]
    attributes: Optional[Dict[str, List[str]]] = None

    def __getitem__(self, item):
        return getattr(self, item)

    def to_dict(self, exclude: List[str] = None) -> dict:
        data = asdict(self)
        data["credentials"] = [asdict(c) for c in self.credentials]
        if exclude:
            for key in exclude:
                data.pop(key, None)
        return data

    def to_json(self, exclude: List[str] = None) -> str:
        return json.dumps(self.to_dict(exclude=exclude), indent=2)


keycloak_base_url = "http://localhost:9099"


def normalize_attributes(raw: Any) -> Optional[Dict[str, List[str]]]:
    if raw is None:
        return None

    # Case 1: wrong shape: list[dict]
    if isinstance(raw, list):
        normalized: Dict[str, List[str]] = {}
        for obj in raw:
            if isinstance(obj, dict):
                for k, v in obj.items():
                    normalized.setdefault(str(k), [])
                    if isinstance(v, list):
                        normalized[str(k)].extend([str(x) for x in v])
                    else:
                        normalized[str(k)].append(str(v))
        return normalized or None

    # Case 2: dict
    if isinstance(raw, dict):
        normalized = {}
        for k, v in raw.items():
            if isinstance(v, list):
                normalized[str(k)] = [str(x) for x in v]
            else:
                normalized[str(k)] = [str(v)]
        return normalized or None

    return {"custom": [str(raw)]}


def get_access_token() -> str:
    keycloak_admin_username = "temp-admin"
    keycloak_admin_password = "temp-admin"

    resp = requests.post(
        f"{keycloak_base_url}/realms/master/protocol/openid-connect/token",
        data={
            "client_id": "admin-cli",
            "username": keycloak_admin_username,
            "password": keycloak_admin_password,
            "grant_type": "password",
        },
        timeout=15,
    )
    resp.raise_for_status()
    return resp.json()["access_token"]


def main():
    with open("users.json", encoding="utf-8") as f:
        raw_list = json.load(f)

    users: List[User] = []
    for u in raw_list:
        attrs = normalize_attributes(u.get("attributes"))
        users.append(
            User(
                username=u["username"],
                firstName=u["firstName"],
                lastName=u["lastName"],
                email=u["email"],
                enabled=u["enabled"],
                credentials=[Credentials(**c) for c in u.get("credentials", [])],
                attributes=attrs,
            )
        )

    access_token = get_access_token()
    auth_headers = {
        "Authorization": f"Bearer {access_token}",
        "Accept": "application/json",
    }

    resp = requests.get(f"{keycloak_base_url}/admin/realms", headers=auth_headers, timeout=15)
    resp.raise_for_status()
    realms = resp.json()

    for r in realms:
        if r["realm"] == "dog-master-realm":
            for user in users:
                payload = user.to_dict()
                try:
                    resp = requests.post(
                        f"{keycloak_base_url}/admin/realms/{r['realm']}/users",
                        json=payload,
                        headers=auth_headers,
                        timeout=30,
                    )
                    if resp.status_code == 201:
                        print(f"Created user: {user.username}")
                    elif resp.status_code == 409:
                        print(f"User already exists: {user.username} (409)")
                    else:
                        resp.raise_for_status()
                except requests.HTTPError as e:
                    print(f"Failed to create {user.username}: {e} | Body: {resp.text}")


if __name__ == "__main__":
    main()

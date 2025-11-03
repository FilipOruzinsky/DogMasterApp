import json
import requests
from dataclasses import dataclass, asdict
from typing import List


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

    def __getitem__(self, item):
        return getattr(self, item)

    def to_dict(self, exclude: List[str] = None) -> dict:
        data = asdict(self)
        # Convert nested dataclasses in credentials
        data["credentials"] = [asdict(c) for c in self.credentials]
        if exclude:
            for key in exclude:
                data.pop(key, None)
        return data

    def to_json(self, exclude: List[str] = None) -> str:
        return json.dumps(self.to_dict(exclude=exclude), indent=2)


keycloak_base_url = "http://localhost:9099"


def main():
    with open("users.json") as f:
        data: list[User] = json.load(f)

        users = [
            User(
                username=u["username"],
                firstName=u["firstName"],
                lastName=u["lastName"],
                email=u["email"],
                enabled=u["enabled"],
                credentials=[
                    Credentials(**c) for c in u["credentials"]
                ]
            )
            for u in data
        ]

        access_token = get_access_token()

        auth_headers = {
            "Authorization": f"Bearer {access_token}",
        }

        resp = requests.get(
            f"{keycloak_base_url}/admin/realms",
            headers=auth_headers,
        )
        resp.raise_for_status()
        realms = resp.json()

        for r in realms:
            if r["realm"] == "dog-master-realm":
                for user in users:
                    resp = requests.post(
                        f"{keycloak_base_url}/admin/realms/{r['realm']}/users",
                        json=user.to_dict(),
                        headers=auth_headers,
                    )
                    resp.raise_for_status()


def get_access_token() -> str:
    keycloak_admin_username = "admin"
    keycloak_admin_password = "admin"

    resp = requests.post(
        f"{keycloak_base_url}/realms/master/protocol/openid-connect/token",
        data={
            "client_id": "admin-cli",
            "username": keycloak_admin_username,
            "password": keycloak_admin_password,
            "grant_type": "password"
        }
    )

    resp.raise_for_status()
    access_token = resp.json()["access_token"]

    print(f'{access_token[:20]}...{access_token[-20:]}')
    print(f'Expires in {resp.json()['expires_in']} seconds')

    return resp.json()["access_token"]


if __name__ == "__main__":
    main()

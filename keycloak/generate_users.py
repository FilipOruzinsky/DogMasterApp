# https://www.keycloak.org/docs-api/latest/rest-api/index.html#UserRepresentation

"""
This script generates users for the DogMasterApp.
It will:
- Generate admin user for the master realm
- Generate users for the dog-master-realm
"""

import json
import requests
from typing import List, Dict, Any, Optional

from Credentials import Credentials
from User import User

keycloak_base_url = "http://localhost:9099"


def main():
    access_token = get_access_token("temp-admin", "temp-admin")
    auth_headers = {
        "Authorization": f"Bearer {access_token}",
        "Accept": "application/json",
    }

    realms = get_realms(auth_headers)

    for r in realms:
        if r["realm"] == "master":
            with open("master-admin-user.json", encoding="utf-8") as f:
                raw_file = json.load(f)
            admin_user = prepare_users_from_file(raw_file)[0]
            user_id = create_user(admin_user, auth_headers, r['realm'])
            assign_role(auth_headers, r['realm'], admin_user, user_id)

        elif r["realm"] == "dog-master-realm":
            with open("users.json", encoding="utf-8") as f:
                raw_file = json.load(f)
            users = prepare_users_from_file(raw_file)
            create_realm_role(auth_headers, r['realm'], "USER")
            for u in users:
                user_id = create_user(u, auth_headers, r['realm'])
                assign_role(auth_headers, r['realm'], u, user_id)


def normalize_attributes(raw: dict[str, List[str]]) -> Dict[str, List[str]]:
    """
    Normalize the attributes of a dictionary by ensuring all keys are converted to strings
    and all values are converted to lists of strings.

    :param raw: A dictionary where keys are strings and values are lists of strings.
    :return: A dictionary with normalized string keys and lists of string values.
    :rtype: Dict[str, List[str]]
    """
    normalized = {}
    for k, v in raw.items():
        normalized[str(k)] = [str(v)]
    return normalized


def get_realms(auth_headers: dict[str, str]) -> Any:
    """
    Retrieve the list of realms from a Keycloak server.

    This function sends a GET request to the Keycloak server's "admin/realms" endpoint
    to retrieve a list of available realms. The request requires appropriate
    authentication headers to be provided.

    :param auth_headers: A dictionary containing authentication headers to authorize
                         the request.
    :return: A JSON object with the response data, containing information about
             the available realms.
    """
    resp = requests.get(f"{keycloak_base_url}/admin/realms", headers=auth_headers)
    resp.raise_for_status()
    return resp.json()


def get_access_token(username: str, password: str) -> str:
    """
    Gets and returns an access token by authenticating against a Keycloak server using the
    provided user credentials. The access token can be used for further requests to the
    Keycloak API. The function performs an HTTP POST request with the necessary authentication
    parameters and processes the response to extract the token.

    :param username: The username for authentication as a string.
    :param password: The password for authentication as a string.
    :return: The access token received from Keycloak as a string.
    """
    keycloak_admin_username = username
    keycloak_admin_password = password

    resp = requests.post(
        f"{keycloak_base_url}/realms/master/protocol/openid-connect/token",
        data={
            "client_id": "admin-cli",
            "username": keycloak_admin_username,
            "password": keycloak_admin_password,
            "grant_type": "password",
        }
    )
    resp.raise_for_status()

    access_token = resp.json()["access_token"]

    print(f'{access_token[:20]}...{access_token[-20:]}')
    print(f'Expires in {resp.json()['expires_in']} seconds')

    return access_token


def assign_role(auth_headers: dict[str, str], realm: str, user: User, user_id: str):
    """
    Assign roles to a user in a specified realm in Keycloak.

    This function iterates through the list of realm roles associated with the specified
    user and assigns each role to the user within the provided realm. It fetches the role
    details using an HTTP GET request to the Keycloak API and assigns the role to the user
    with an HTTP POST request. The function also ensures the proper status is returned
    for the assignment process.

    :param auth_headers: A dictionary containing the authorization headers is required
                         to interact with the Keycloak API.
    :param realm: The name of the Keycloak realm where the roles will be assigned.
    :param user: An instance of the User class representing the user whose roles
                 are to be assigned.
    :param user_id: The unique identifier of the user within the Keycloak realm.
    :return: None
    """
    for role_name in user.realmRoles:
        role = requests.get(
            f"{keycloak_base_url}/admin/realms/{realm}/roles/{role_name}",
            headers=auth_headers,
        ).json()
        print(f"Role: {role}")
        assign = requests.post(
            f"{keycloak_base_url}/admin/realms/{realm}/users/{user_id}/role-mappings/realm",
            headers=auth_headers,
            json=[{"id": role["id"], "name": role["name"]}],
        )
        assign.raise_for_status()
        print(f"Assigned {role_name} to {user_id[:5]}...{user_id[-5:]}")


def create_user(user: User, auth_headers: dict[str, str], realm_name: str) -> str:
    """
    Creates a new user in the specified realm by sending a request to the Keycloak server.

    This function performs a POST request to the Keycloak Admin REST API to create a user
    in the given realm using the provided user object, authentication headers, and realm name.
    The location of the created user in the response headers is used to extract the user ID,
    which is then returned.

    :param user: A User object containing the details of the user to be created.
    :type user: User
    :param auth_headers: A dictionary of headers, including authentication credentials, required
        for making the request.
    :type auth_headers: dict[str, str]
    :param realm_name: The name of the Keycloak realm where the user is to be created.
    :type realm_name: str
    :return: The unique ID of the newly created user as returned by the Keycloak server.
    :rtype: str
    """
    resp = requests.post(
        f"{keycloak_base_url}/admin/realms/{realm_name}/users",
        json=user.to_dict(),
        headers=auth_headers,
    )
    resp.raise_for_status()
    location = resp.headers["Location"]

    user_id = location.rsplit("/", 1)[-1]
    return user_id


def prepare_users_from_file(file: Any) -> List[User]:
    """
    Prepares a list of `User` objects from the provided file-like object containing
    user data. The method processes each user entry, extracts relevant data,
    normalizes their attributes, and instantiates `User` objects accordingly.

    :param file: An iterable object containing user data, each element being a
        dictionary with user details.
    :return: A list of `User` objects created from the provided file data.
    :rtype: List[User]
    """
    users: List[User] = []
    for user_in_file in file:
        attrs = normalize_attributes(user_in_file.get("attributes"))
        users.append(User(
            username=user_in_file["username"],
            firstName=user_in_file["firstName"],
            lastName=user_in_file["lastName"],
            email=user_in_file["email"],
            enabled=user_in_file["enabled"],
            credentials=[Credentials(**c) for c in user_in_file.get("credentials", [])],
            attributes=attrs,
            realmRoles=[r for r in user_in_file.get("realmRoles", [])],
        ))
    return users


def create_realm_role(auth_headers: dict[str, str], realm: str, role_name: str, description: Optional[str] = None):
    """
    Creates a new realm role in the Keycloak system for the specified realm.

    This function sends a POST request to the Keycloak admin API to create a new
    role within the given realm. The name of the role is required, while the
    description is optional. If the request is successful, the role will be created.

    :param auth_headers: Dictionary containing authentication headers, such as
        an access token. These headers will be included in the API request.
    :type auth_headers: dict[str, str]
    :param realm: Name of the realm where the role will be created. It specifies
        the context in which the role should exist.
    :type realm: str
    :param role_name: Name of the role to be created. This serves as the unique
        identifier for the role within the realm.
    :type role_name: str
    :param description: Optional parameter that provides a textual description
        of the role.
    :type description: Optional[str]
    :return: None
    """
    url = f"{keycloak_base_url}/admin/realms/{realm}/roles"
    payload = {"name": role_name}
    if description:
        payload["description"] = description
    r = requests.post(url, headers={**auth_headers, "Content-Type": "application/json"}, json=payload)
    if r.status_code not in (201, 204):
        r.raise_for_status()
        print(f"Failed to create role {role_name}")
    print(f"Created role {role_name}")


if __name__ == "__main__":
    main()

"""Initialize a fresh local SonarQube demo without printing credentials."""

from __future__ import annotations

import base64
import json
import os
from pathlib import Path
import secrets
from urllib.parse import urlencode
from urllib.request import Request, urlopen


SERVER = "http://localhost:9000"
AUTH_FILE = Path(__file__).with_name(".sonar-local.env")


def post(path: str, username: str, password: str, fields: dict[str, str]) -> dict:
    credentials = base64.b64encode(f"{username}:{password}".encode()).decode()
    request = Request(
        SERVER + path,
        data=urlencode(fields).encode(),
        headers={
            "Authorization": f"Basic {credentials}",
            "Content-Type": "application/x-www-form-urlencoded",
        },
        method="POST",
    )
    with urlopen(request, timeout=30) as response:
        body = response.read()
    return json.loads(body) if body else {}


def main() -> None:
    if AUTH_FILE.exists():
        raise SystemExit("Local credentials already exist; refusing to overwrite .sonar-local.env")

    with urlopen(SERVER + "/api/system/status", timeout=10) as response:
        status = json.load(response)["status"]
    if status != "UP":
        raise SystemExit(f"SonarQube is {status}; retry when the server is UP")

    admin_password = secrets.token_urlsafe(32)
    post(
        "/api/users/change_password",
        "admin",
        "admin",
        {"login": "admin", "previousPassword": "admin", "password": admin_password},
    )
    flags = os.O_WRONLY | os.O_CREAT | os.O_EXCL
    fd = os.open(AUTH_FILE, flags, 0o600)
    with os.fdopen(fd, "w") as output:
        output.write(f"SONAR_ADMIN_PASSWORD={admin_password}\n")
    post(
        "/api/projects/create",
        "admin",
        admin_password,
        {"project": "m08-checkout-sast", "name": "M08 Checkout SAST demo", "visibility": "private"},
    )
    token = post(
        "/api/user_tokens/generate",
        "admin",
        admin_password,
        {"name": "m08-local-demo"},
    )["token"]
    with AUTH_FILE.open("a") as output:
        output.write(f"SONAR_TOKEN={token}\n")
        output.write(f"SONARQUBE_TOKEN={token}\n")
    print("Created project m08-checkout-sast and local credentials in .sonar-local.env")


if __name__ == "__main__":
    main()

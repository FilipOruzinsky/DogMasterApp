import json
from dataclasses import dataclass, asdict
from typing import List, Optional, Dict

from Credentials import Credentials


@dataclass
class User:
    username: str
    firstName: str
    lastName: str
    email: str
    enabled: bool
    credentials: List[Credentials]
    attributes: Optional[Dict[str, List[str]]] = None
    realmRoles: Optional[List[str]] = None

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

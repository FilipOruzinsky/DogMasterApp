from dataclasses import dataclass


@dataclass
class Credentials:
    type: str
    value: str
    temporary: bool

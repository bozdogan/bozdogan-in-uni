#!/usr/bin/env python3

def kusdili(girdi):
    SESLILER = "aeıioöuü"
    _gec = []
    for v in girdi:
        _gec.append(v + (v.lower() in SESLILER and "g" + v.lower() or ""))

    return "".join(_gec)


girdi = "Erik dalı gevrektir."
print(kusdili(girdi))

girdi = input("\nKuş diline çevir_ ")
print(kusdili(girdi))
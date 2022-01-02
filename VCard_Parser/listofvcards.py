#!/usr/bin/env python3
import sys, os, os.path


def getpath():
    if len(sys.argv) > 1:
        return sys.argv[1:]
    else:
        return [input("Please type the location of vCF file:\n >_ ")]

print(os.path.isfile("rehber"))

# Bozdogan Bilisim A.S.
if __name__ == '__main__':
    paths = getpath()

    information = []
    for pIndex in range(len(paths)):
        _currentfile = paths[pIndex]
        if os.path.isfile(_currentfile):
            with open(_currentfile) as f:
                information.append(f.read())
        else:
            print("WARNING: The file '{}' cannot found, so omitted.".format(_currentfile))
    information = "".join(information)

    people = []
    _state = 0 # (0=Outside, 1=Inside) of person definition.
    _index = 0
    for line in information.split("\n"):
        if line == "BEGIN:VCARD":
            people.append({})
            _state = 1
        if line == "END:VCARD":
            _index += 1
            _state = 0

        # in the definition
        if _state == 1 and line.find(":") != -1:
            k,v = line.split(":")
            people[_index][k] = v

    for person in people:
        # print("-"*30)
        # for k,v in person.items():
        #     print(str(k)+": "+str(v))
        # print("-"*30)
        if "FN" in person.keys():
            namekw = "FN"
        elif "N" in person.keys():
            namekw = "N"
        else:
            namekw = None

        print(person["TEL;CELL"], (namekw and person[namekw] or ""))

    print("\nEND OF FILE")
    os.system("PAUSE")



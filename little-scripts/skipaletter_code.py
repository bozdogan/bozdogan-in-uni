#!/usr/bin/env python3

"""
Some fun thing for, you know, cipher messages.
"""
# Bozdogan Bilisim A.Ş.

def encode(msg: str) -> str:
    _result = []
    punc = ('.', '!', '?', '\n') #punctuation used to seperate sentences
    statements = [] #container for sentences

    # Seperate sentences in msg.
    currleft = 0
    for i in range(len(msg)):
        if(msg[i] in punc):
            statements.append(msg[currleft:i+1])
            currleft = i+1

    #
    for s in statements:
        for word in s.split():
            # If word end with punctuation, ignore it on process and append after it.
            _ending = word[-1:] in punc and word[-1:] or ""
            if _ending: word = word[:-1]

            # If the word is lowercase, just do your thing.
            # If the word is UPPERCASE, cast to upper after process.
            # If the word is MixedCase, just do your thing.
            # If the word begins Upper and goes lower, do your thing and capitalize first letter.
            CAPITAL, UPPER = not word.islower() and word[1:].islower(), word.isupper()

            coded = ""
            word = word[:len(word)//2], word[len(word)//2:]
            # print("  DEBUG",*word)

            for i in range(len(word[1])):
                if len(word[0]) != i:
                    coded += word[0][i]
                coded += word[1][i]

            if CAPITAL: # Capital-case
                coded = coded[0].upper() + coded[1:]
            if UPPER: # UPPERCASE
                coded = coded.upper()

            coded += _ending and _ending+" " or " "
            _result.append(coded)


    return "".join(_result)


def decode(msg: str) -> str:
    _result = []
    punc = ('.', '!', '?', '\n') #punctuation used to seperate sentences
    statements = [] #container for sentences

    # Seperate sentences in msg.
    currleft = 0
    for i in range(len(msg)):
        if(msg[i] in punc):
            statements.append(msg[currleft:i+1])
            currleft = i+1

    #
    for s in statements:
        for word in s.split():
            # If word end with punctuation, ignore it on process and append after it.
            _ending = word[-1:] in punc and word[-1:] or ""
            if _ending: word = word[:-1]

            decoded = ["",""]

            for i in range(len(word)//2):
                decoded[0] += word[2*i]
                decoded[1] += word[2*i+1]
            if len(word)%2 == 1:
                decoded[1] += word[-1:]
            decoded = "".join(decoded)

            decoded += _ending and _ending + " " or " "
            _result.append(decoded)


    return "".join(_result)


if __name__ == "__main__":
    args = __import__("sys").argv

    print("  Skip-A-Letter Code Utility v0.1")
    operation = "E"
    message = ""

    if len(args)>3:
        operation = args[1].lower() in ("e","encode") and "E" or "D"
        message = " ".join(args[2:])
    else:
        operation = input("[E]ncode or [D]ecode?_ ")
        operation = operation.lower() in ("e","encode") and "E" or "D"

        message = input("Type message_ ")

    if operation == "E":
        print("ENCODED:", encode(message))
    else:
        print("DECODED:", decode(message))

    print("-"*42)

# Eirk dlaı geekvtrir. Eiln kzıı nkatziir.
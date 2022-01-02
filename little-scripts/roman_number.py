#!/usr/bin/env python3

# ------------------------------------------------------- #
# Autogenerated, do not edit. All changes will be undone. #
# ------------------------------------------------------- #

# roman literals for numbers
r1="I"
r5="V"
r10="X"
r50="L"
r100="C"
r500="D"
r1000="M"
r5000="v"
r10000="x"

def rroman(number):
    """Converts an integer input to a string containing roman numeral representation of it.
    :param number: number whose roman literal will be generated
    :return: roman numeral
    """

    # only works in the integer range of 1 to 3999. create a list and insert
    # partial roman repr_s till all decimal places handled. then reverse the
    # list -since appending has started from one's place- and merge it to a
    # string.
    if not 1 <= number <= 39999:
        return None # should i throw an exc_?

    roman_list = []

    digits= []
    while number > 0:
        digits.append(number % 10)
        number //= 10

    def inner_roman(num, base1, base5, base_next):
        """
        helper function for positioning roman literals
        given current and next base literals, generates roman numeral for the digit
        :param num: current digit
        :param base1: current 1's literal
        :param base5: current 5's literal
        :param base_next: next 1's literal
        :return: partial roman numeral
        """

        # if num == 0 then return ""
        if num < 4:
            return num * base1
        elif num == 4:
            return base1+base5
        elif num == 5:
            return base5
        elif num < 9:
            return base5 + (num-5)*base1
        elif num == 9:
            return base1 + base_next

    try:
        roman_list.append(inner_roman(digits[0],r1,r5,r10)) # ones digit
        roman_list.append(inner_roman(digits[1],r10,r50,r100)) # tens digit
        roman_list.append(inner_roman(digits[2],r100,r500,r1000)) # hundreds digit
        roman_list.append(inner_roman(digits[3],r1000,r5000,r10000)) # thousands digit
    except IndexError:
        pass # index error for digits array w/ small numbers. nothing to worry about.

    roman_list.reverse()
    return "".join(roman_list)

if __name__ == '__main__':
    while True:
        num: int
        try:
            __ = input("Number to represent in roman notation\n >_ ")
            if not __:
                break
            num = int(__)
        except ValueError:
            print("\n!!!\n")

        print(rroman(num))

# END OF roman_number.py
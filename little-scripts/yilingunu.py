#!/usr/bin/env python3

if __name__ == "__main__":
    numofdays = 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    
    day = int(input("(Integer between 0-366) >_ "))
    day = 366 if day==366 else day % 365
    print("input:",day,sep="")
    
    leap = day>31
    
    m = 0
    while day > numofdays[m]:
        day -= numofdays[m]
        m += 1
        if m==12: m = 0
    
    print("m:{:02} d:{:02}".format(m+1, day))
    
    if leap:
        day -= 1
        if day<=0:
            m -= 1
            day = numofdays[m]
        if m==-1: m = 11
        print("artık yıl ise m:{:02} d:{:02}".format(m+1, day))
    #__import__("os").system("PAUSE")

# tarih: 2019-03-25
# yazar: b.ozdogan_
# yilingunu.py SONU
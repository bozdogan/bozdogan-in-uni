import random, time, sys, os
try:
	os.system("title 01000111 01000101 01010010 01000011 01000101 01001011 "
			"00100000 01000010 01001111 01011010 01000100 01001111 01000111 01000001 01001110"
			"& color 0A") # green text, black background
	print(*[str(int(random.random()+0.5)) for i in range(17020)],"", end="")
	while True:
		print(*[str(int(random.random()+0.5)) for i in range(120*2-10)],"", end="")
		sys.stdout.flush()
		time.sleep(.142)
except KeyboardInterrupt:
	os.system("color 07 & title") # back to normal colors
os.system("color 07 & title") # back to normal colors
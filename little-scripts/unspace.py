import sys
import os.path

file_list = []
for arg in sys.argv[1:]:
    if os.path.isfile(arg):
        file_list.append(arg)

counter_of_success = 0
try:
    for fpath in file_list:
        dpath, fname = os.path.split(fpath)
        new_fname = fname.replace(" ", "_")

        print("in dir {}\n    {} --> {}".format(dpath, fname, new_fname))
        os.rename(fpath, os.sep.join((dpath, new_fname)))
        counter_of_success += 1

except OSError:
    print("\n# An error occured while renaming {}.".format(file_list[counter_of_success]))

print("\n{}/{} files renamed.".format(counter_of_success, len(file_list)))

# unspace.py SONU

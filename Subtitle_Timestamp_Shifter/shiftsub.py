#!/usr/bin/env python3
import sys
"""
subshift 0.1
Subtitle time shift and scale utility by bozdogan
---
Changelog 0.1:
@2019-06-10
* Initial release
* 'Shifting timestamps in a file' functionality added.
* '.srt' subtitle file support added.
* UTF-8 with and without BOM encodings (and possibly most 
  ASCII derivatives) are supported.
* Output file can be specified.
  - Extension of output file cannot be '.mp4', just in case.
* Duration to be added can be specified as seconds or
  milliseconds.
* Now negative durations are supported, too.
"""


def uniform_time(time: str) -> int:
    """Timestamp as total milliseconds from the beginning"""
    #> 'time': '00:00:16,682'
    uniform = 0
    time = time.split(",")
    time = time[0].split(":") + [time[1]]

    uniform += int(time[3])  # ms
    uniform += int(time[2])*1000  # s
    uniform += int(time[1])*60*1000  # m
    uniform += int(time[0])*60*60*1000  # h

    return uniform


def subfile_time(uniform: int) -> str:
    """Time format of subtitle files"""
    subtime = [uniform % 1000]
    uniform //= 1000
    subtime.append(uniform % 60)  # seconds
    uniform //= 60
    subtime.append(uniform % 60)  # minutes
    uniform //= 60
    subtime.append(uniform % 60)  # hours

    time_builder = ["{:02}".format(x) for x in reversed(subtime[1:])]

    #> 'time': '00:00:17,938'
    return ":".join(time_builder)+","+"{:03}".format(subtime[0])


def _parse_args():
    DIGITS = ("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    args = {"__": []}
    flag = None
    for arg in sys.argv[1:]:
        if flag:
            args[flag] = arg
            flag = None
        elif arg.startswith("-") and arg[1] not in DIGITS:
            flag = arg
        else:
            args["__"].append(arg)

    return args


def _parse_duration(duration):
    # Remove 's' and 'ms' postfixes at the end and convert it to number type.
    if duration.endswith("s"):
        duration = int(duration[:-2]) if duration.endswith("ms") else int(duration[:-1])*1000
    else:
        duration = int(duration)

    return duration


if __name__ == '__main__':
    """ '.str' file format (for each line) is as follows:
    [row number]
    [start time] --> [end time]
    [body]
    [blank line]
    ---
    1
    00:00:16,682 --> 00:00:17,938
    Hello, old friend.
    
    2
    00:01:05,056 --> 00:01:06,432
    Bernard.
    
    """
    VIDEO_EXT = (".mp4", ".avi", ".mkv")
    args = _parse_args()

    #@ test args
    #args = {"__": ['testsubfile.srt', '1850ms']}

    # Argument parsing
    infile = args["__"][0]
    duration = _parse_duration(args["__"][-1])
    scale = 1  # TODO: future feature
    outfile = "-o" in args and args["-o"] or infile

    # Prevent accidentally replace a movie file.
    for ext in VIDEO_EXT:
        if outfile.endswith(ext):
            outfile += ".srt"
            break

    has_bom = False  # Encoding of input file is UTF-8 with BOM

    # Reading the lines. A line is four piece of information:
    # row index, start time, end time and line body.
    lines = []
    with open(infile) as file:
        while True:
            row_index = file.readline().strip()
            if not row_index:  # EOF reached
                break
            # Check beginning of the file, remove BOM if present
            if len(lines) == 0 and row_index.startswith("ï»¿"):
                has_bom = True
                row_index = row_index[3:]

            start_time, end_time = file.readline().strip().split(" --> ")
            body = []
            while True:
                line = file.readline()
                if not line or line == "\n":  # EOF or empty line
                    break
                body.append(line)

            # Process the line and insert it to the list
            lines.append({
                "row_index": row_index,
                "start_time": uniform_time(start_time)*scale+duration,
                "end_time": uniform_time(end_time)*scale+duration,
                "body": body,
            })

    with open(outfile, "w") as file:
        if has_bom:
            file.write("ï»¿")
        for line in lines:
            file.write(line["row_index"]+"\n"+
                       subfile_time(line["start_time"])+" --> "+
                       subfile_time(line["end_time"])+"\n"+
                       "".join(line["body"])+"\n")

    # for line in lines:
    #     locals().update(line)
    #     print("row_index:", row_index)
    #     print("start_time end_time:", subfile_time(start_time+duration), subfile_time(end_time+duration))
    #     print("body:")
    #     print("\n".join(body))

    def _convtimes(el: dict):
        el["start_time"] = subfile_time(el["start_time"])
        el["end_time"] = subfile_time(el["end_time"])
        return el
    lines = map(_convtimes, lines)
    print(*("{row_index:>4}:\t{start_time} --> {end_time}\t{body}".format(**line) for line in lines), sep="\n")

    print("\nFile is ready: "+outfile)


# last contribution @2019-06-10 by bozdogan
# END OF shiftsub.py
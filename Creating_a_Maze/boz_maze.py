#!/usr/bin/env python3

from newengine import *
from random import randrange

"""creates visual mazes of any size"""

# -- PREFERENCES --
APP_NAME = "Programming Mazes"
MAZE_SIZE = (24, 16)  # (W, H)
DELAY = 0.01 

CELLWIDTH = 3
WALLWIDTH = 1
PIXEL_SIZE = (8, 8)

B_DRAW_STACK_TRACE = True
BT_DELAY = DELAY*.5


# -- CONSTANTS --
CELL_PATH_N = 0x01
CELL_PATH_E = 0x02
CELL_PATH_S = 0x04
CELL_PATH_W = 0x08
CELL_VISITED = 0x10
CELL_ON_STACK = 0x20

# -- GLOBAL VARS --
maze = [0x00] * (MAZE_SIZE[0]*MAZE_SIZE[1])
stack = []
n_visited_cells = 0

# -- engine control vars --
_fps_rate = 0  # fps label update rate counter
b_pause_enabled = False  # set True to activate step-by-step mode
b_paused = True


# --
# -- APP LOGIC -- #
def init(eng: GameEngine):
    global n_visited_cells, b_paused

    # Start at random location
    x = randrange(MAZE_SIZE[0])
    y = randrange(MAZE_SIZE[1])

    maze[y*MAZE_SIZE[0] + x] = CELL_VISITED | CELL_ON_STACK
    stack.append((x, y))
    n_visited_cells = 1

    def keydown(e):
        global b_paused
        if e.key == pygame.K_e:
            b_paused = False

    eng.set_event_handler(pygame.KEYDOWN, keydown)

    return True


def loop(eng: GameEngine):
    global _fps_rate, n_visited_cells, b_pause_enabled, b_paused

    # Update fps label once every .1 sec to improve readability
    if _fps_rate > 0:
        _fps_rate -= eng.elapsed_time
    else:
        _fps_rate = .1
        eng.title = "{} | FPS: {}".format(APP_NAME, eng.fps)

    maze_width, maze_height = MAZE_SIZE
    b_processing = n_visited_cells < maze_width * maze_height

    # ==
    # == MAZE ALGORITHM == #

    if b_processing and not (b_pause_enabled and b_paused):
        if b_pause_enabled:
            b_paused = True

        def offset(x, y):
            # Index of a cell relative to top of the stack
            return (stack[-1][1] + y) * maze_width + stack[-1][0] + x

        # Create a Set of Unvisited Neighbors
        neighbors = []

        if stack[-1][1] > 0 and maze[offset(0, -1)] & CELL_VISITED == 0:
            neighbors.append(0)  # 0 for NORTH

        if stack[-1][0] < maze_width-1 and maze[offset(1, 0)] & CELL_VISITED == 0:
            neighbors.append(1)  # 1 for EAST

        if stack[-1][1] < maze_height-1 and maze[offset(0, 1)] & CELL_VISITED == 0:
            neighbors.append(2)  # 2 for SOUTH

        if stack[-1][0] > 0 and maze[offset(-1, 0)] & CELL_VISITED == 0:
            neighbors.append(3)  # 3 for WEST

        # Pick a random neighbor if available and go that direction
        if len(neighbors) > 0:
            next_dir = neighbors[randrange(len(neighbors))]

            # create a path between cells
            if next_dir == 0:  # N
                maze[offset(0, -1)] |= CELL_VISITED | CELL_ON_STACK | CELL_PATH_S
                maze[offset(0, 0)] |= CELL_PATH_N
                stack.append((stack[-1][0] + 0, stack[-1][1] - 1))

            elif next_dir == 1:  # E
                maze[offset(1, 0)] |= CELL_VISITED | CELL_ON_STACK | CELL_PATH_W
                maze[offset(0, 0)] |= CELL_PATH_E
                stack.append((stack[-1][0] + 1, stack[-1][1] + 0))

            elif next_dir == 2:  # S
                maze[offset(0, 1)] |= CELL_VISITED | CELL_ON_STACK | CELL_PATH_N
                maze[offset(0, 0)] |= CELL_PATH_S
                stack.append((stack[-1][0] + 0, stack[-1][1] + 1))

            elif next_dir == 3:  # W
                maze[offset(-1, 0)] |= CELL_VISITED | CELL_ON_STACK | CELL_PATH_E
                maze[offset(0, 0)] |= CELL_PATH_W
                stack.append((stack[-1][0] - 1, stack[-1][1] + 0))

            # as one of if block above executes, algorithm enters a new cell
            n_visited_cells += 1

            # Increase satisfaction
            if DELAY > 0:
                time.sleep(DELAY)

        else:
            # No neighbors -> Backtrack
            maze[offset(0, 0)] &= ~CELL_ON_STACK
            stack.pop()

            if DELAY > 0:
                time.sleep(BT_DELAY)  # satisfaction++;

    # ==
    # == DRAW == #

    background = Colors.BLACK
    unvisited = Colors.BLUE
    visited = Colors.WHITE
    active = Colors.GREEN
    on_stack = (180, 255, 180)  # lighter green

    # Clear screen
    eng.draw_rect((0, 0), eng.canvas_size,background)

    for y in range(maze_height):
        for x in range(maze_width):
            current_cell = maze[y*maze_width + x]
            cell_size = CELLWIDTH + WALLWIDTH
            color = unvisited

            # Draw cells
            if current_cell & CELL_VISITED:
                color = visited

            if B_DRAW_STACK_TRACE and current_cell & CELL_ON_STACK:
                color = on_stack

            eng.draw_rect((x*cell_size, y*cell_size), (CELLWIDTH, CELLWIDTH), color)

            # Draw paths
            if current_cell & CELL_PATH_E:
                wall_color = color
                if B_DRAW_STACK_TRACE and (x+1, y) not in stack:
                    wall_color = visited

                eng.draw_rect(((x+1)*cell_size - WALLWIDTH, y*cell_size),
                              (WALLWIDTH, CELLWIDTH), wall_color)

            if current_cell & CELL_PATH_S:
                wall_color = color
                if B_DRAW_STACK_TRACE and (x, y+1) not in stack:
                    wall_color = visited

                eng.draw_rect((x*cell_size, (y+1)*cell_size - WALLWIDTH),
                              (CELLWIDTH, WALLWIDTH), wall_color)

    if b_processing:
        # Highlight current cell
        x, y = stack[-1]
        eng.draw_rect((x*(CELLWIDTH+WALLWIDTH), y*(CELLWIDTH+WALLWIDTH)),
                      (CELLWIDTH, CELLWIDTH), active)

    elif len(stack) > 0:
        # Empty the stack
        if DELAY > 0:
            time.sleep(BT_DELAY)  # satisfaction++;

        x, y = stack.pop()
        maze[y*maze_width + x] &= ~CELL_ON_STACK

    return True


# int main(){
if __name__ == '__main__':
    WINDOW_SIZE = (MAZE_SIZE[0]*(CELLWIDTH+WALLWIDTH) - WALLWIDTH,
                   MAZE_SIZE[1]*(CELLWIDTH+WALLWIDTH) - WALLWIDTH)
    game = GameEngine()

    if game.construct(*WINDOW_SIZE, *PIXEL_SIZE, init, loop):
        game.start()
# }

# by bozdogan
# at 2019-09-08
# END OF boz_maze.py

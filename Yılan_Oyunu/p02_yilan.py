#!/usr/bin/env python3

import random
from typing import List
from newengine import *

# Direction constants
NORTH, EAST, SOUTH, WEST = 0, 1, 2, 3

class Cell:
    x: int
    y: int

    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y

    def __str__(self):
        return "<{}, {}>".format(self.x, self.y)

    def __eq__(self, other):
        return isinstance(other, Cell) \
               and self.x == other.x and self.y == other.y

start_x = int((40 + 3) / 2)
start_y = int(40 / 2)

class Levels:
    # Level = walls: List[Cell], snake: List[Cell], snake_dir
    Level1 = [], \
             [Cell(22, 20),
              Cell(21, 20),
              Cell(20, 20),
              Cell(19, 20),
              Cell(18, 20)], EAST

    Level2 = [Cell(0, 0),
              Cell(0, 1),
              Cell(1, 0)], \
             [Cell(22, 20),
              Cell(21, 20),
              Cell(20, 20),
              Cell(19, 20),
              Cell(18, 20)], EAST

# Cell size
CELL_W, CELL_H = 16, 16

# Board size
WIDTH, HEIGHT = 30, 30

# Coordinate list of walls
walls: List[Cell]
# Coordinates of food
food: Cell
# Coordinate list of snake cells
snake: List[Cell]
# 0: NORTH, 1: EAST, 2: SOUTH, 3: WEST
snake_dir: int

# Snake speed in units per second
snake_spd: int = 10

# - Control variables -
snake_move_delay: float = 1/snake_spd  # initial delay
just_changed_dir: bool = False
score: int = 0
game_over: bool = False

fps_update_rate = 0


def spawn_food():
    global walls, food, snake
    available_cells = []
    for y in range(HEIGHT):
        for x in range(WIDTH):
            cell = Cell(x, y)
            if not (cell in walls or cell in snake):
                available_cells.append(cell)

    food = available_cells[random.randint(0, len(available_cells))]


def engine_create(game: GameEngine):
    global walls, snake, snake_dir

    # INITIALIZE THE LEVEL
    walls, snake, snake_dir = Levels.Level1

    # Create the first food
    spawn_food()

    # DEFINE INPUT HANDLER
    def handle_input(event):
        global snake_dir, just_changed_dir

        if event.key == pygame.K_RIGHT:
            # change snake's direction to west
            if not just_changed_dir and snake_dir in (NORTH, SOUTH):
                snake_dir = EAST
                just_changed_dir = True

        elif not just_changed_dir and event.key == pygame.K_LEFT:
            # change snake's direction to east
            if snake_dir in (NORTH, SOUTH):
                snake_dir = WEST
                just_changed_dir = True

        elif not just_changed_dir and event.key == pygame.K_UP:
            # change snake's direction to north
            if snake_dir in (EAST, WEST):
                snake_dir = NORTH
                just_changed_dir = True

        elif not just_changed_dir and event.key == pygame.K_DOWN:
            # change snake's direction to east
            if snake_dir in (EAST, WEST):
                snake_dir = SOUTH
                just_changed_dir = True

        # @TODO sil şunu xdsdfadsf
        elif event.key == pygame.K_z:
            game._set_size_vars((WIDTH+10)*CELL_W, (HEIGHT+10)*CELL_H, 1, 1)
            game.canvas = pygame.Surface(game.canvas_size)
            game.screen = pygame.display.set_mode(game.screen_size)

    # Bind input handler
    game.set_event_handler(pygame.KEYDOWN, handle_input)
    return True


def engine_update(game: GameEngine):
    global snake_spd, snake_move_delay, just_changed_dir, \
        score, game_over, fps_update_rate

    if fps_update_rate <= 0:
        fps_update_rate = .1
        game.title = "Yılaan | FPS: {}".format(game.fps)
    else:
        fps_update_rate -= game.elapsed_time

    if game_over:
        # unbind input listener
        game.set_event_handler(pygame.KEYDOWN, None)

    else:
        #
        # GAME MECHANICS

        if snake_move_delay >= 0:
            snake_move_delay -= game.elapsed_time
        else:
            #
            # MOVE

            # reset move timer and direction lock
            snake_move_delay = 1/snake_spd
            just_changed_dir = False

            # 1) head of snake moves one unit
            head = snake[0]
            prev_head = Cell(head.x, head.y)
            if snake_dir == NORTH:
                head.y -= 1
            elif snake_dir == EAST:
                head.x += 1
            elif snake_dir == SOUTH:
                head.y += 1
            elif snake_dir == WEST:
                head.x -= 1

            # possible new cell location
            prev_tail = snake[-1]

            # 2) the rest follows the head
            for i in range(len(snake)-1, 1, -1):
                snake[i] = snake[i-1]
            snake[1] = prev_head

            # Wrap around when out of bounds
            if head.x < 0 and snake_dir == WEST:
                # Exits to left
                head.x = WIDTH-1

            if head.x >= WIDTH and snake_dir == EAST:
                # Exits to right
                head.x = 0

            if head.y < 0 and snake_dir == NORTH:
                # Exits to top
                head.y = HEIGHT-1

            if head.y >= HEIGHT and snake_dir == SOUTH:
                # Exits to bottom
                head.y = 0

            #
            # CHECK COLLISIONS

            # Eaten food
            if head == food:
                snake.append(prev_tail)
                score += snake_spd
                spawn_food()

            # Collide self
            for cell in snake[1:]:
                if head == cell:
                    game_over = True

            # Collide a wall
            for cell in walls:
                if head == cell:
                    game_over = True

    #
    # DRAWING

    # Draw board
    game.draw_rect((0, 0), (WIDTH * CELL_W, HEIGHT * CELL_H), Colors.DARK_BLUE)

    # Draw walls
    for cell in walls:
        game.draw_rect((cell.x*CELL_W, cell.y*CELL_H), (CELL_W, CELL_H), Colors.BLACK)

    # Draw food
    game.draw_rect((food.x*CELL_W, food.y*CELL_H), (CELL_W, CELL_H), Colors.RED)

    # Draw snake
    for cell in snake:
        game.draw_rect((cell.x*CELL_W+1, cell.y*CELL_H+1), (CELL_W-2, CELL_H-2), Colors.WHITE)

    game.print(20, 20, ("GAME OVER! " if game_over else "")+"Score: {}".format(score), fontsize=20)

    return True


def main():
    game = GameEngine()

    if game.construct(WIDTH * CELL_W, HEIGHT * CELL_H, 1, 1, engine_create, engine_update):
        game.start()


if __name__ == "__main__":
    main()

# Last edit: 2019-07-02 by bozdogan
# END OF p02_yilan.py

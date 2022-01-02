#!/usr/bin/env python3

from engine import *
from pygame.locals import *

class BouncingBall(BozPixelGE):
    def __init__(self):
        super().__init__()

        self.framerate = 0

        self.pos = [self.canvas_size[0]/2, self.canvas_size[1]/2]  # center
        self.vel = [240, 220]
        self.ball_img: pygame.Surface

        self.speed_check_timer = 0
        self.speed_check_label_refresh_timer = 0
        self.check0 = None
        self.check1 = 0, 0
        self.max_fps = 0

    def on_engine_create(self):
        self.ball_img: pygame.Surface = pygame.image.load("intro_ball.gif")

        return True

    def speed_check(self):
        self.check0 = self.check1
        self.check1 = self.pos[0], self.pos[1]

    def on_engine_update(self):
        # Clear canvas
        self.draw_rect((0, 0), self.canvas_size, Colors.DARK_BLUE)

        self.title = "Bouncing Ball | FPS: {}".format(self.fps)

        # @ check if the ball really goes 100px/s
        self.speed_check_timer += self.elapsed_time
        if self.speed_check_timer >= 1:
            self.speed_check_timer = 0
            self.speed_check()

        _velX = _velY = "N/A"
        if self.check0 is not None:
            _velX = int(self.check1[0] - self.check0[0])
            _velY = int(self.check1[1] - self.check0[1])

        self.print(10, 10, "current xVel: {}".format(_velX))
        self.print(10, 25, "current yVel: {}".format(_velY))

        self.max_fps = max(self.fps, self.max_fps)
        self.print(200, 25, "MAX FPS: {}".format(self.max_fps))

        #
        # User input

        #
        # Update position

        # displacement for current frame
        dx, dy = self.vel[0] * self.elapsed_time, \
                 self.vel[1] * self.elapsed_time
        self.pos[0] += dx
        self.pos[1] += dy

        #
        # Collusion handling: BALL
        ball = pygame.Rect(self.pos, self.ball_img.get_size())
        if ball.x < 0 or ball.x+ball.w > self.screen_size[0]:
            # Collides left or right wall
            self.pos[0] -= dx
            self.vel[0] = -self.vel[0]

        if ball.y < 0 or ball.y+ball.h > self.screen_size[1]:
            # Collides top or bottom wall
            self.pos[1] -= dy
            self.vel[1] = -self.vel[1]

        #
        # Draw
        self.draw(self.pos, self.ball_img)
        # self.draw_rect((ball.x, ball.y), ball.size, Colors.RED, 2)

        return True


if __name__ == '__main__':
    eng = BouncingBall()
    if eng.construct(700, 500):
        eng.start()

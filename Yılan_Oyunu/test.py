#!/usr/bin/env python3
from engine import BozPixelGE


class TestEngine(BozPixelGE):
    def __init__(self):
        super().__init__()
        self.framerate = 30

    def on_engine_update(self):
        self.title = "FPS: "+str(self.fps)

        self.draw_rect((0, 0), self.canvas_size, (0, 0, 128))
        self.print(10, 20, "elapsed_time: " + str(self.elapsed_time / 1000) + "sec since last frame")
        self.print(10, 40, "canvas dimensions: {} x {} (w/{}x{}pts)".format(*self.canvas_size, *self.point_size))
        self.print(10, 60, "screen dimensions: {} x {}".format(*self.screen_size))
        return True


if __name__ == '__main__':
    game_engine = TestEngine()
    game_engine.construct(340, 200, 2, 3)

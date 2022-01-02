#!/usr/bin/env python3

import time
with __import__("contextlib").redirect_stdout(None):
    import pygame  # silently .d


class Colors:
    """RGB color codes for common colors"""
    BLACK = (0, 0, 0)
    WHITE = (255, 255, 255)
    RED = (255, 0, 0)
    BLUE = (0, 0, 255)
    DARK_BLUE = (0, 0, 128)
    GREEN = (0, 255, 0)


class GameEngine:
    def __init__(self):
        self.canvas_size = self.screen_size = self.point_size = 0, 0
        self.framerate = 0  # 0 for uncapped frame rate

        self.clock = [0, 0]
        self.screen: pygame.Surface = None
        self.canvas: pygame.Surface = None

        def _on_quit(_):
            # Handle close event (when user clicks X button on the titlebar)
            self.ended = True

        self.event_handlers = {pygame.QUIT: _on_quit}
        self.started = False
        self.ended = False

    def construct(self, width, height, pt_width=1, pt_height=1,
                  on_create=None, on_update=None):
        """ Construct the screen.

        :param width: Canvas width in points
        :param height: Canvas height in points
        :param pt_width: Point width (in pixels)
        :param pt_height: Point height (in pixels)
        :param on_create: Function to override ´on_engine_create´ method
        :param on_update: Function to override ´on_engine_update´ method"""

        # Set size variables
        self._set_size_vars(width, height, pt_width, pt_height)

        # Create surfaces
        self.canvas = pygame.Surface(self.canvas_size)
        self.screen = pygame.display.set_mode(self.screen_size)

        # Bind custom methods if provided
        if on_create:
            self.on_engine_create = lambda: on_create(self)
        if on_update:
            self.on_engine_update = lambda: on_update(self)

        self.clock = [time.clock()] * 2
        self.start()

    def _set_size_vars(self, width, height, pt_width, pt_height):
        self.canvas_size = width, height
        self.point_size = pt_width, pt_height
        self.screen_size = width * pt_width, height * pt_height

    def start(self):
        if self.screen is None:
            print("Screen surface does not exist. (Probably "
                  "´start´ is called before ´construct´.)")
            return False

        # Trigger initialization
        if not self.on_engine_create():
            return False

        self.started = True

        # Game loop
        self.clock[0], self.clock[1] = self.clock[1], time.perf_counter()  # clock tick
        while not self.ended and self.on_engine_update():
            # Handle events
            for event in pygame.event.get():
                for t, handler in self.event_handlers.items():
                    if event.type == t:
                        handler(event)

            # Draw the canvas into screen
            pygame.transform.scale(self.canvas, self.screen_size, self.screen)
            pygame.display.flip()
            # clock tick
            self.clock[0], self.clock[1] = self.clock[1], time.perf_counter()

        # @TODO make it spawn another thread and run the game loop in there, \
        #  and then return true if thread started successfully.
        return True

    def set_event_handler(self, event_type, handler):
        """Assign or change the handler for a particular event.
        ´handler´ takes 1 argument, which is event."""
        if handler is None:
            if event_type in self.event_handlers:
                del self.event_handlers[event_type]
        else:
            self.event_handlers[event_type] = handler

    @property
    def title(self):
        """pygame window title"""
        return pygame.display.get_caption() if self.started else None

    @title.setter
    def title(self, val):
        if self.started:
            pygame.display.set_caption(val)

    # Useful clock properties
    @property
    def elapsed_time(self) -> float:
        """Time passed since last frame (seconds)"""
        return self.clock[1] - self.clock[0]

    @property
    def fps(self) -> int:
        """Average FPS of last 10 frames"""
        return int(1 / self.elapsed_time)

    # Drawing methods
    def draw(self, pos, source: pygame.Surface):
        """Draw a ´Surface´ on canvas."""
        self.canvas.blit(source, pos)

    def draw_rect(self, pos, size, color, bd_width=0):
        """Draw a rectangle on canvas. If border width isn't specified,
        it'll fill the shape."""
        pygame.draw.rect(self.canvas, color, (*pos, *size), bd_width)

    def draw_circ(self, pos, color, radius, bd_width=0):
        """Draw a circle on canvas. If border width isn't specified,
        it'll fill the shape."""
        pygame.draw.circle(self.canvas, color, pos, radius, bd_width)

    def print(self, x, y, text, fg_color=Colors.WHITE, bg_color=None,
              fontname="Consolas", fontsize=14):
        pygame.font.init()
        fontface = pygame.font.SysFont(fontname, fontsize)
        srf = fontface.render(text, False, fg_color, bg_color)
        self.canvas.blit(srf, (x, y))

    # User methods
    def on_engine_create(self) -> bool:
        """This method is called once, after initializing the display.
        Load your surfaces here.

        If returns ´False´, the engine will stop working."""
        return True

    def on_engine_update(self) -> bool:
        """This method is called every frame.

        If returns ´False´, the engine will stop working."""

        self.title = "Bozdogan Pixel Game Engine | FPS: {}".format(self.fps)
        return True

# Last edit: 2019-07-07 by bozdogan
# END OF newengine.py

"""
Usage:
    bot [options] (run | build | reboot)
    bot (-i | --interactive)
    bot (-h | --help | --version)
Options:
    -i, --interactive  Interactive Mode
    -h, --help  Show this screen and exit.
"""

import sys
import cmd
import os

from docopt import docopt, DocoptExit


def docopt_cmd(func):
    """
    This decorator is used to simplify the try/except block and pass the result
    of the docopt parsing to the called action.
    """

    def fn(self, arg):
        try:
            opt = docopt(fn.__doc__, arg)

        except DocoptExit as e:
            # The DocoptExit is thrown when the args do not match.
            # We print a message to the user and the usage block.

            print('Invalid Command!')
            print(e)
            return

        except SystemExit:
            # The SystemExit exception prints the usage for --help
            # We do not need to do the print here.

            return

        return func(self, opt)

    fn.__name__ = func.__name__
    fn.__doc__ = func.__doc__
    fn.__dict__.update(func.__dict__)
    return fn


class MyInteractive(cmd.Cmd):
    intro = 'Welcome ' + os.environ['USER'] \
            + '\nAsk me for Help at any moment'
    prompt = 'Roc-bot@: '

    nohelp = 'Sorry, ask jens to fix the help for %s'

    @docopt_cmd
    def do_run(self, arg):
        """Usage: run [-a]

        Options:
            -a, --attached  Does not detach the screen
        """

        print("Running with args:")
        print("\t", arg)

    @docopt_cmd
    def do_build(self, arg):
        """Usage: build [-a]

        Options:
            -a, --attached  Does not detach the screen"""

        print("Building with args:")
        print("\t", arg)

    @docopt_cmd
    def do_reboot(self, arg):
        """Usage: reboot [-a]

        Options:
            -a, --attached  Does not detach the screen"""

        print("Rebooting with args:")
        print("\t", arg)

    def do_clear(self, arg):
        """Clears the screen"""
        for _ in range(50):
            print()

    def do_exit(self, arg):
        """Puts the bot to sleep"""

        print('Now i will sleep')
        exit()


opt = docopt(__doc__, sys.argv[1:])

if opt['--interactive']:
    MyInteractive().cmdloop()

print(opt)

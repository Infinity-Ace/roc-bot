"""
Usage:
    bot (run | build | reboot)
    bot (-i | --interactive)
    bot (-h | --help | --version)
Options:
    -i, --interactive  Interactive Mode
    -h, --help  Show this screen and exit.
"""

import sys
import cmd
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
    intro = 'Welcome master' \
            + '\nAsk me for Help at any moment'
    prompt = 'Roc-bot:$ '

    @docopt_cmd
    def do_run(self, arg):
        """Usage: run"""

        print("Running with args:")
        print(arg)

    @docopt_cmd
    def do_build(self, arg):
        """Usage: build"""

        print("Building with args:")
        print(arg)

    @docopt_cmd
    def do_reboot(self, arg):
        """Usage: reboot"""

        print("Reboot with args:")
        print(arg)

    def do_exit(self, arg):
        """Puts the bot to sleep"""

        print('Now i will sleep')
        exit()

opt = docopt(__doc__, sys.argv[1:])

if opt['--interactive']:
    MyInteractive().cmdloop()

print(opt)

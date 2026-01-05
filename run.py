#!/usr/bin/env python3
"""
Development entry point script for Negative Space Puzzler.
Provides convenient commands for building, testing, and running the app.
"""

import subprocess
import sys
import os

def run_gradle(command: str, *args):
    """Run a Gradle command.

    Args:
        command: The Gradle task to execute (e.g., 'build', 'test').
        *args: Additional arguments to pass to the Gradle command.

    Returns:
        subprocess.CompletedProcess: The result of the subprocess execution.
    """
    gradle_wrapper = "./gradlew.bat" if os.name == "nt" else "./gradlew"
    full_command = [gradle_wrapper, command, "--no-daemon"] + list(args)
    print(f"Running: {' '.join(full_command)}")
    return subprocess.run(full_command)

def main():
    """Parse command-line arguments and execute the corresponding command.

    Reads the first command-line argument and dispatches to the appropriate
    Gradle task or help function. Exits with the return code from Gradle
    commands, or with code 1 for unknown commands.
    """
    if len(sys.argv) < 2:
        print_help()
        return
    
    command = sys.argv[1].lower()
    
    commands = {
        "build": lambda: run_gradle("build"),
        "test": lambda: run_gradle("testDebugUnitTest"),
        "coverage": lambda: run_gradle("jacocoTestReport"),
        "lint": lambda: run_gradle("lint"),
        "apk": lambda: run_gradle("assembleDebug"),
        "clean": lambda: run_gradle("clean"),
        "install": lambda: run_gradle("installDebug"),
        "help": print_help,
    }
    
    if command in commands:
        result = commands[command]()
        if hasattr(result, 'returncode'):
            sys.exit(result.returncode)
    else:
        print(f"Unknown command: {command}")
        print_help()
        sys.exit(1)

def print_help():
    """Print the help message showing available commands and usage examples."""
    help_text = """
Negative Space Puzzler - Development Scripts
=============================================

Available commands:
  build    - Build the project
  test     - Run unit tests
  coverage - Generate test coverage report
  lint     - Run lint checks
  apk      - Build debug APK
  clean    - Clean build artifacts
  install  - Install on connected device
  help     - Show this help message

Usage: python run.py <command>

Examples:
  python run.py build
  python run.py test
  python run.py apk
"""
    print(help_text)

if __name__ == "__main__":
    main()

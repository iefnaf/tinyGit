# tinyGit

tinyGit is a version-control system that mimics some of the basic features of the popular system Git. It implements many basic commands of git, such as add, remove, branch, merge, etc. By learning the source code of tinyGit, you can understand the essence of git and it will be no longer mysterious to you.

tinyGit comes from cs61b, an awesome course about data structures. Thanks to Professor Hug for opening up the course-related materials, I learned a lot from this course.

**Warning**: If you are taking this course and doing this project, I strongly recommend that you close this page and implement it yourself.

## Build tinyGit from Source

### Prerequisites

* JDK 1.8 or higher
* make
* python

### Clone

Clone the source code to your development machine.

```shell
$ git clone git@github.com:iefnaf/tinyGit.git
```

### Build

Build the tinyGit from the source code.

``` shell
$ cd tinyGit
$ make
```

### Run

``` shell
$ java gitlet.Main <COMMAND> <OPERAND1> <OPERAND2> ...
```

## Supported commands

* **init** : Creates a new gitlet version-control system in the current directory. 
* **add** : Adds a copy of the file as it currently exists to the *staging area*.
* **commit** : Saves a snapshot of tracked files in the current commit and staging area.
* **rm** : Unstage the file if it is currently staged for addition.
* **log** : Starting at the current head commit, display information about each commit.
* **global-log** : Displays information about all commits ever made
* **find** : Prints out the ids of all commits that have the given commit message.
* **status** : Displays what branches currently exist, and marks the current branch with a `*`, and what files have been staged for addition or removal.
* **checkout** : Checkout out the specified file or branch.
* **branch** : Creates a new branch with the given name, and points it at the current head commit.
* **rm-branch** : Deletes the branch with the given name.
* **reset** : Checks out all the files tracked by the given commit.
* **merge** : Merges files from the given branch into the current branch.

## Learn about tinyGit

TBD

## Links

* [CS 61B sp21](https://sp21.datastructur.es/)
* [Gitlet project](https://sp21.datastructur.es/materials/proj/proj2/proj2#testing)
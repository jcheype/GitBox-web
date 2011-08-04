#! /usr/bin/env python

import os
import re
import sys
from pymongo import Connection

ssh_command = os.environ['SSH_ORIGINAL_COMMAND']
user = sys.argv[1]

m = re.search("^(git-receive-pack|git-upload-pack) '(\w+)\.git'$", ssh_command)
if m == None:
    exit(1)

command = m.group(1)
repoName = m.group(2)

connection = Connection('localhost', 27017)

db = connection.gitbox
repoCollection = db["Repository"]

repo = repoCollection.find_one({"name" : repoName, "writeUsers" : user})
if repo == None and command == "git-upload-pack":
    repo = repoCollection.find_one({"name" : repoName, "readUsers" : user})

if repo != None:
    arg = "%s 'repo/%s.git'" %(command, repoName)
    os.execv("/usr/bin/git-shell", ["/usr/bin/git-shell","-c", arg])

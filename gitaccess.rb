#! /usr/bin/env ruby

require 'rubygems'  # not necessary for Ruby 1.9
require 'mongo'

#git-receive-pack 'toto.git'


user  = ARGV[0]

ssh_command = ENV['SSH_ORIGINAL_COMMAND']
#puts ssh_command

if ssh_command =~ /^(git-receive-pack|git-upload-pack) '(\w+)\.git'$/
  command = $1
  repoName = $2
else
  exit(1)
end
  
db = Mongo::Connection.new.db("gitbox")
coll = db["Repository"]
repository = coll.find({"name" => repoName, "writeUsers" => user}).first()
if repository == nil and command == "git-upload-pack"
  repository = coll.find({"name" => repoName, "readUsers" => user}).first()
end

if repository != nil
  exec("git-shell -c \"#{command} 'repo/#{repository["owner"]}/#{repoName}.git'\"")
end
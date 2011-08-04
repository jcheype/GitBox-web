GitBox Web
==========

This web application let you manage git repositories.
You can easily create a repository and add user access (read/write) to your repo.

Each users can their own set of SSH public keys.

Installation
-------------

In order to install, GitBox-web on your server you'll have to:

* [install playframework](http://www.playframework.org/)
* [install mongodb](http://www.mongodb.org/) (very simple installation: just need to donwload and launch server)
* add a git user on you server (no password)
* choose one of the 2 script gitaccess (python or ruby) and install pymongo or mongo-ruby
* make a symbolic link as git user:


       chmod +x gitaccess.rb
       ln -s gitaccess.rb $HOME/gitaccess
    

and voilà

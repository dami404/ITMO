#!/bin/bash

BACKUP_SERVER_PATH=postgres3@pg168:/var/db/postgres3/backups/
BACKUP_NAME=backup_$(date +"%Y-%m-%d-%H-%M")

pg_basebackup -p 9807 -h localhost --username=postgres0 -D $HOME/basebackups/$BACKUP_NAME -Ft -z -Xs

scp -r $HOME/basebackups/$BACKUP_NAME $BACKUP_SERVER_PATH
find $HOME/basebackups -type d -name "backup_*" -mtime +7 -exec rm -r {} +
find $HOME/ehz35/pg_wal -type f -name "*" -mtime +7 -exec rm -r {} +

find $HOME/backups -type d -name "backup_*" -mtime +30 -exec rm -r {} +




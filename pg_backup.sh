#pg dump
PGPASSWORD="Start123!" pg_dump -h localhost -p 5432 -U postgres tn-cert > backup-`date +"%Y-%m-%d_%H.%M.%S"`.sql
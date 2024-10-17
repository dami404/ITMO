<?php 
    global $db_connect;
    if (!$db_connect) {
        $dsn = "pgsql:host=pg" .
                ";port=5432;dbname=studs" .
                ";user=" .
                ";password=" ;
        try {
            $db_connect = new PDO($dsn);
        } catch (PDOException $exception) {
            $msg = $exception->getMessage();
            echo $msg . 
                    ". Do not forget to enable in the web server the database 
                    manager for php and in the database instance authorize the 
                    ip of the server instance if they not in the same 
                    instance.";
        }
    }
    
?>
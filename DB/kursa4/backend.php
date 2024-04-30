<?php
global $db_connect;
include_once "db.php";

$headers = getallheaders();
// if ($headers['REQUEST_TYPE']) {

    // switch ($headers['REQUEST_TYPE']) {
    //     case 'create_order':
            $date = $_POST['date'];
            $comment = $_POST['comment'];
            $technique = $_POST['technique'];
            // $req = $db_connect->prepare('SELECT get_owners(:user_id)');
            // $req->bindValue(':user_id',$_COOKIE['USER_ID']);
            // $req->execute();
            // $obj = $req->fetch(\PDO::FETCH_ASSOC);

            $req = $db_connect->prepare('SELECT id FROM techniqueType where name=:technique');

            $req->bindValue(':technique',$technique);

            $req->execute();

            $technique_id = $req->fetch(\PDO::FETCH_ASSOC)['id'];


            $req = $db_connect->prepare('SELECT create_order(:client_id,:technique_id,:order_content,:order_cost,:date)');
            $req->bindValue(':client_id',$_COOKIE['USER_ID']);
            $req->bindValue(':technique_id',$technique_id);
            $req->bindValue(':order_content',$comment);
            $req->bindValue(':order_cost',1000);
            $req->bindValue(':date',$date);
            $req->execute();




            // break;
    // }
// }

?>
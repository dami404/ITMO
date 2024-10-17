<?php
include_once "db.php";
switch ($_POST["action_type"]) {
    case 'create_order':
        $date = $_POST["date"];
        $comment = $_POST["comment"];
        $technique = $_POST["technique"];
        // echo $date;
        try {
        $req = $db_connect->prepare('SELECT get_masters(:date)');
        $req->bindValue(':date',$date);
        $req->execute();
        $table = [];
        while ($obj = $req->fetch(\PDO::FETCH_ASSOC)) {
            $obj['get_masters'] = trim($obj['get_masters'], '()');
            $ar = explode(",",$obj['get_masters']);
            $table[] = ['id' => $ar[0], 'rating' => $ar[1]];
        }
        } catch (Exception $e) {
            echo $e->getMessage();
        }
        // echo $date;
        if (count($table) > 0) {
            try {
            $req = $db_connect->prepare('SELECT create_order(:client_id,:technique_id,:order_content,:order_cost,:date)');
            $req->bindValue(':client_id',$_COOKIE['USER_ID']);
            $req->bindValue(':technique_id',$technique);
            $req->bindValue(':order_content',$comment);
            $req->bindValue(':order_cost',1000);
            $req->bindValue(':date',$date);
            $req->execute();

            $req = $db_connect->prepare('select currval(:name)');
            $req->bindValue(':name','s338923.orders_id_seq');
            $req->execute();
            $obj = $req->fetch(\PDO::FETCH_ASSOC);

            $req = $db_connect->prepare('SELECT add_order(:order_id, :master_id)');
            $req->bindValue(':order_id',$obj['currval']);
            $req->bindValue(':master_id',intval(current($table)['id']));
            $req->execute();
            }catch (Exception $e) {
                echo $e->getMessage();
            }
        } else {
            ?>
            <script>alert('Нет свободных мастеров на эту дату')</script>
            <?php
        }
        header('Location: https://se.ifmo.ru/~s338923/isbd/index.php');
        break;
    case 'create_order_master':
        $masterId = $_POST["masterId"];
        $date = $_POST["date"];
        $comment = $_POST["comment"];
        $technique = $_POST["technique"];
        
        try {
            $req = $db_connect->prepare('SELECT create_order_with_master(:client_id,:technique_id,:order_content,:order_cost,:date,:masterId)');
            $req->bindValue(':client_id',$_COOKIE['USER_ID']);
            $req->bindValue(':technique_id',$technique);
            $req->bindValue(':order_content',$comment);
            $req->bindValue(':order_cost',1000);
            $req->bindValue(':date',$date);
            $req->bindValue(':masterId',$masterId);
            $req->execute();
        }catch (Exception $e) {
            echo $e->getMessage();
        }
        header('Location: https://se.ifmo.ru/~s338923/isbd/index.php');
        break;
    case 'rate_order':
        $order = $_POST["order_id"];
        $comment = $_POST["comment"];
        $rating = $_POST["rating"];
        try {
        $req = $db_connect->prepare('SELECT leave_feedback(:client_id,:order_id,:order_content,:rating)');
        $req->bindValue(':client_id',$_COOKIE['USER_ID']);
        $req->bindValue(':order_id',$order);
        $req->bindValue(':order_content',$comment);
        $req->bindValue(':rating',$rating);
        $req->execute();
        } catch(Exception $e) {
            echo $e->getMessage();
        }
        header('Location: https://se.ifmo.ru/~s338923/isbd/index.php');
        break;
    case 'add_technique':
        $date = $_POST["date"];
        $technique = $_POST["technique"];
        try {
        $req = $db_connect->prepare('SELECT add_technique(:client_id,:date,:technique)');
        $req->bindValue(':client_id',$_COOKIE['USER_ID']);
        $req->bindValue(':date',$date);
        $req->bindValue(':technique',$technique);
        $req->execute();
        } catch(Exception $e) {
            echo $e->getMessage();
        }
        header('Location: https://se.ifmo.ru/~s338923/isbd/add.php');
        break;
    case 'new_subscribe':
        $start_date = $_POST["start_date"];
        $finish_date = $_POST["finish_date"];
        $plan_id = $_POST["technique"];
        try {
        $req = $db_connect->prepare('SELECT subscribe_client_plan(:client_id,:plan_id,:start_date,:finish_date)');
        $req->bindValue(':client_id',$_COOKIE['USER_ID']);
        $req->bindValue(':start_date',$start_date);
        $req->bindValue(':finish_date',$finish_date);
        $req->bindValue(':plan_id',$plan_id);
        $req->execute();
        } catch(Exception $e) {
            echo $e->getMessage();
        }
        header('Location: https://se.ifmo.ru/~s338923/isbd/subscribe.php');
        break;
    case 'qa':
        $theme = $_POST["theme"];
        $comment = $_POST["comment"];
        try {
        $req = $db_connect->prepare('SELECT leave_question(:client_id,:theme,:comment)');
        $req->bindValue(':client_id',$_COOKIE['USER_ID']);
        $req->bindValue(':theme',$theme);
        $req->bindValue(':comment',$comment);
        $req->execute();
        } catch(Exception $e) {
            echo $e->getMessage();
        }
        header('Location: https://se.ifmo.ru/~s338923/isbd/qa.php');
        break;
    case 'schedule':
        $days = $_POST["days"];
        print_r($_POST);
        try {
        $req = $db_connect->prepare('SELECT delete_schedule(:master_id)');
        $req->bindValue(':master_id',$_COOKIE['USER_ID']);
        $req->execute();

        foreach ($days as $day) {
            $req = $db_connect->prepare('SELECT add_day(:master_id, :day_id)');
            $req->bindValue(':master_id',$_COOKIE['USER_ID']);
            $req->bindValue(':day_id',$day);
            $req->execute();
        }
        } catch(Error $e) {
            echo $e->getMessage();
        }
        header('Location: https://se.ifmo.ru/~s338923/isbd/profile.php');
        break;
}
?>
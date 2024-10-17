<?php 
include_once "header.php";
if ($_GET["action"] && $_GET['id']) {
    if ($_GET["action"] == 'approve') {
        $req = $db_connect->prepare('SELECT accept_order(:order_id,:master_id)');
        $req->bindValue(':master_id',$_COOKIE['USER_ID']);
        $req->bindValue(':order_id',$_GET['id']);
        $req->execute();
    } elseif ($_GET["action"] == 'reject') {
        $req = $db_connect->prepare('SELECT reject_order(:order_id,:master_id)');
        $req->bindValue(':master_id',$_COOKIE['USER_ID']);
        $req->bindValue(':order_id',$_GET['id']);
        $req->execute();

        try {
            $req = $db_connect->prepare('SELECT get_masters(:date)');
            $req->bindValue(':date',$_GET['date']);
            $req->execute();
            $table = [];
            while ($obj = $req->fetch(\PDO::FETCH_ASSOC)) {
                $obj['get_masters'] = trim($obj['get_masters'], '()');
                $ar = explode(",",$obj['get_masters']);
                $table[] = [$ar[0]];
            }
            $index = array_search($_COOKIE['USER_ID'], $table);
            if ($index == count($table)-1) {
                $req = $db_connect->prepare('SELECT cancel_order(:order_id)');
                $req->bindValue(':order_id',$_GET['id']);
                $req->execute();
            } else {
                $req = $db_connect->prepare('SELECT add_order(:order_id, :master_id)');
                $req->bindValue(':order_id',$_GET['id']);
                $req->bindValue(':master_id',intval($table[$index+1]));
                $req->execute();
            }
        } catch (Exception $e) {
            echo $e->getMessage();
        }

    } elseif ($_GET["action"] == 'finish') {
        $req = $db_connect->prepare('SELECT finish_order(:order_id,:master_id)');
        $req->bindValue(':master_id',$_COOKIE['USER_ID']);
        $req->bindValue(':order_id',$_GET['id']);
        $req->execute();
    }
    // header('Location: https://se.ifmo.ru/~s338923/isbd/orders.php');
}
?>
<div class='main'>
    <h1>Заказы мастера</h1>
    <h2>Новые заказы</h2>
    <div class="list">
        <?php
        try {
            $req = $db_connect->prepare('SELECT get_master_new_orders(:user_id)');
            $req->bindValue(':user_id',$_COOKIE['USER_ID']);
            $req->execute();
            while ($obj = $req->fetch(\PDO::FETCH_ASSOC)){
                $obj['get_master_new_orders'] = trim($obj['get_master_new_orders'], '()');
                $ar = explode(",",$obj['get_master_new_orders']);
        ?>
                <div class="item">
                    <div class='order'>
                        <a href="/~s338923/isbd/orders.php?action=approve&id=<?php echo $ar[0];?>">Принять заказ</a>
                        <a href="/~s338923/isbd/orders.php?action=reject&id=<?php echo $ar[0];?>&date=<?php echo $ar[3];?>">Отклонить заказ</a>
                        <!-- +статус -->
                        <p>Заказ №<?php echo $ar[0];?> Статус - <?php echo $ar[4];?></p>
                        <!-- +Тип техники -->
                        <p>Тип техники: <?php echo $ar[5];?></p>
                        <p>Дата ремонта: <?php echo date("d.m.Y", strtotime($ar[3]));?></p>
                        <!-- +Клиент -->
                        <p>Клиент: <?php echo $ar[6];?></p>
                        <p>Цена: <?php echo $ar[2];?>р</p>
                        <p>Комментарий: <?php echo $ar[1];?></p>
                    </div>
                </div>
        <?php
            }
        }catch (Exception $e) {
            echo $e->getMessage();
        }
        ?>
    </div>
    <h2>Активные заказы</h2>
    <div class="list">
        <?php
            $req = $db_connect->prepare('SELECT get_master_current_orders(:user_id)');
            $req->bindValue(':user_id',$_COOKIE['USER_ID']);
            $req->execute();
            while ($obj = $req->fetch(\PDO::FETCH_ASSOC)){
                $obj['get_master_current_orders'] = trim($obj['get_master_current_orders'], '()');
                $ar = explode(",",$obj['get_master_current_orders']);
        ?>
        
                <div class="item">
                    <div class='order'>
                        <a href="/~s338923/isbd/orders.php?action=finish&id=<?=$ar[0]?>">Завершить заказ</a>
                        <!-- +статус -->
                        <p>Заказ №<?php echo $ar[0];?> Статус - <?php echo $ar[4];?></p>
                        <!-- +Тип техники -->
                        <p>Тип техники: <?php echo $ar[5];?></p>
                        <p>Дата ремонта: <?php echo date("d.m.Y", strtotime($ar[3]));?></p>
                        <!-- +Клиент -->
                        <p>Клиент: Иванов Иван</p>
                        <p>Цена: <?php echo $ar[2];?>р</p>
                        <p>Комментарий: <?php echo $ar[1];?></p>
                    </div>
                </div>
        <?php
            }
        ?>
    </div>
    <h2>История заказов</h2>
    <div class="list">
        <?php
            $req = $db_connect->prepare('SELECT get_master_order_history(:user_id)');
            $req->bindValue(':user_id',$_COOKIE['USER_ID']);
            $req->execute();
            while ($obj = $req->fetch(\PDO::FETCH_ASSOC)){
                $obj['get_master_order_history'] = trim($obj['get_master_order_history'], '()');
                $ar = explode(",",$obj['get_master_order_history']);
        ?>
                <div class="item">
                    <div class='order'>
                        <!-- +статус -->
                        <p>Заказ №<?php echo $ar[0];?> Статус - <?php echo $ar[5];?></p>
                        <!-- +Тип техники -->
                        <p>Тип техники: <?php echo $ar[6];?></p>
                        <p>Дата ремонта: <?php echo date("d.m.Y", strtotime($ar[3]));?></p>
                        <!-- +Клиент -->
                        <p>Клиент: <?php echo $ar[7];?></p>
                        <p>Цена: <?php echo $ar[2];?>р</p>
                        <p>Комментарий: <?php echo $ar[1];?></p>
                    </div>
                </div>
        <?php
            }
        ?>
    </div>
</div>
<?php include_once "footer.php";?>

<?php 
include_once "header.php";
include_once "db.php";
global $db_connect;
?>
<div class='main'>
    <h1>Заказы мастера</h1>
    <h2>Новые заказы</h2>
    <div class="list">


        <div class="item">
            <div class='order'>
                <a href="/~s338923/isbd/orders.php?approve=y&id=1">Принять заказ</a>
                <a href="/~s338923/isbd/orders.php?aprove=n&id=1">Отклонить заказ</a>
                <p>Заказ №1 Статус - Выполняется</p>
                <p>Тип техники: Посудомойка</p>
                <p>Дата ремонта: 29.12.2023</p>
                <p>Клиент: Иванов Иван</p>
                <p>Цена: 1000р</p>
                <p>Комментарий: Протекает посудомойка</p>
            </div>
        </div>
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
                    <a href="/~s338923/isbd/orders.php?finish=y&id=1">Завершить заказ</a>
                    <!-- +статус -->
                    <p>Заказ №<?php echo $ar[0];?> Статус - Выполняется</p>
                    <!-- +Тип техники -->
                    <p>Тип техники: Посудомойка</p>
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
        <!-- <div class="item">
            <div class='info'>
                <a href="/~s338923/isbd/orders.php?finish=y&id=1">Завершить заказ</a>
                <p>Заказ №1 Статус - Выполняется</p>
                <p>Тип техники: Посудомойка</p>
                <p>Дата ремонта: 29.12.2023</p>
                <p>Клиент: Иванов Иван</p>
                <p>Цена: 1000р</p>
                <p>Комментарий: Протекает посудомойка</p>
            </div>
        </div> -->
        <!-- <div class="item">
            <div class='info'>
                <a href="/~s338923/isbd/orders.php?finish=y&id=1">Завершить заказ</a>
                <p>Заказ №1 Статус - Выполняется</p>
                <p>Тип техники: Посудомойка</p>
                <p>Дата ремонта: 29.12.2023</p>
                <p>Клиент: Иванов Иван</p>
                <p>Цена: 1000р</p>
                <p>Комментарий: Протекает посудомойка</p>
            </div>
        </div> -->
        <!-- <div class="item">
            <div class='info'>
                <a href="/~s338923/isbd/orders.php?finish=y&id=1">Завершить заказ</a>
                <p>Заказ №1 Статус - Выполняется</p>
                <p>Тип техники: Посудомойка</p>
                <p>Дата ремонта: 29.12.2023</p>
                <p>Клиент: Иванов Иван</p>
                <p>Цена: 1000р</p>
                <p>Комментарий: Протекает посудомойка</p>
            </div>
        </div> -->
    </div>
    <h2>История заказов</h2>
    <div class="list">
        <div class="item">
            <div class='order'>
                <p>Заказ №1 Статус - Выполняется</p>
                <p>Тип техники: Посудомойка</p>
                <p>Дата ремонта: 29.12.2023</p>
                <p>Клиент: Иванов Иван</p>
                <p>Цена: 1000р</p>
                <p>Комментарий: Протекает посудомойка</p>
            </div>
        </div>
    </div>
</div>
<?php include_once "footer.php";?>

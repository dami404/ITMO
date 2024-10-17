<?php 
include_once "header.php";
include_once "db.php";
global $db_connect;
?>
<div class='main'>
    <h1>Заказы</h1>
    <div class="btn_block">
        <button class="btn" onclick="showForm(this)" <?php if($_GET['masterId']):?>style="display: none;"<?php endif?>>Создать заказ</button>
        <?php $stmt = $db_connect->prepare('SELECT get_owners(:user)');

        $stmt->bindValue(':user', $_COOKIE['USER_ID']);
        // execute the statement
        $stmt->execute();
        ?>
        <form method='POST' class='save-order-form' id='form' action="backend.php" <?php if($_GET['masterId']):?>style="display: flex;"<?php endif?>>
            <?php if($_GET['masterId']) {?>
                <input type="text" hidden name="action_type" value="create_order_master">
                <input type="text" hidden name="masterId" value="<?=$_GET['masterId']?>">
            <?php }else {?>
            <input type="text" hidden name="action_type" value="create_order">
            <?php }?>
            <div class="element">
                <label for="date">Дата, когда приедет мастер</label>
                <input type="date" name='date' required>
            </div>
            <div class="element">
                <label for="comment">Опишите проблему</label>
                <input type="text" name='comment' required>
            </div>
            <div class="element">
                <label for="technique">Выберите прибор</label>
                <select  name="technique" required>
                <?php while ($obj = $stmt->fetch(\PDO::FETCH_ASSOC)) {
                    $obj['get_owners'] = trim($obj['get_owners'], '()');
                    $ar = explode(",",$obj['get_owners']);
                    ?>
                    <option value="<?=$ar[0]?>"><?=$ar[2]?></option>
                <?php }?>
                </select>
            </div>
            <input type="submit" class='btn' value="Сохранить">
        </form>
    </div>
    <h2>Активные заказы</h2>
    <div class="list">
        <?php
        // id заказа
        // статус заказа
        // тип техники(название)
        // дата ремонта
        // мастер
        // оплата
        // комментарий
        $stmt = $db_connect->prepare('SELECT get_client_current_orders(:user)');

        $stmt->bindValue(':user', $_COOKIE['USER_ID']);
        // execute the statement
        $stmt->execute();

        // return the result set as an object
        while ($obj = $stmt->fetch(\PDO::FETCH_ASSOC)) {
            $obj['get_client_current_orders'] = trim($obj['get_client_current_orders'], '()');
            $ar = explode(",",$obj['get_client_current_orders']);
            // var_dump($ar);
            ?>
            <div class="item">
                <div class='info'>
                    <p>Заказ №<?= $ar[0]?> Статус - <?= $ar[4]?></p>
                    <p>Тип техники: <?= $ar[6]?></p>
                    <p>Дата ремонта: <?= $ar[3]?></p>
                    <p>Мастер: <?= $ar[5]?></p>
                    <p>Оплата: <?= $ar[2]?></p>
                    <p>Комментарий: <?= $ar[1]?></p>
                </div>
            </div>
        <?php }?>
    </div>
    <h2>История заказов</h2>
    <div class="list">
        <?php
        // id заказа
        // статус заказа
        // тип техники(название)
        // дата ремонта
        // мастер
        // оплата
        // комментарий
        $stmt = $db_connect->prepare('SELECT get_client_history_orders(:user)');

        $stmt->bindValue(':user', $_COOKIE['USER_ID']);
        // execute the statement
        $stmt->execute();

        // return the result set as an object
        while ($obj = $stmt->fetch(\PDO::FETCH_ASSOC)) {
            $obj['get_client_history_orders'] = trim($obj['get_client_history_orders'], '()');
            $ar = explode(",",$obj['get_client_history_orders']);
            // var_dump($ar);
            ?>
            <div class="item">
                <div class='info'>
                    <?php if ($ar[4] == 'Завершен') {?>
                    <a href="/~s338923/isbd/rate.php?id=<?= $ar[0]?>">Оценить заказ</a>
                    <?php }?>
                    <p>Заказ №<?= $ar[0]?> Статус - <?= $ar[4]?></p>
                    <p>Тип техники: <?= $ar[6]?></p>
                    <p>Дата ремонта: <?= $ar[3]?></p>
                    <p>Мастер: <?= $ar[5]?></p>
                    <p>Оплата: <?= $ar[2]?></p>
                    <p>Комментарий: <?= $ar[1]?></p>
                </div>
            </div>
        <?php }?>
    </div>
</div>
<?php include_once "footer.php";?>

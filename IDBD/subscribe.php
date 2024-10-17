<?php 
include_once "header.php";
include_once "db.php";
global $db_connect;
?>
<div class='main'>
    <h1>Подписка</h1>
    <div class="btn_block">
        <button class="btn" onclick="showForm(this)">Оформить новую подписку</button>
        <form action="backend.php" id='form' class="subscribe-form" method="post">
        <?php $stmt = $db_connect->query('SELECT get_subscribe_plans()');
        ?>
        <input type="text" hidden name="action_type" value="new_subscribe">
            <div class="element">
                <label for="start_date">Дата начала подписки</label>
                <input type="date" name='start_date' required>
            </div>
            <div class="element">
                <label for="finish_date">Дата окончания подписки</label>
                <input type="date" name='finish_date' required>
            </div>
            <div class="element">
                <label for="technique">Выберите план</label>
                <select  name="technique" required>
                    <?php while ($obj = $stmt->fetch(\PDO::FETCH_ASSOC)) {
                        $obj['get_subscribe_plans'] = trim($obj['get_subscribe_plans'], '()');
                        $ar = explode(",",$obj['get_subscribe_plans']);
                        ?>
                        <option value="<?=$ar[0]?>"><?=$ar[1]?></option>
                    <?php }?>
                </select>
            </div>
            <input type="submit" class='btn' value="Сохранить">
        </form>
    </div>
    <div class="list">
    <?php
        $req = $db_connect->prepare('SELECT findall_subscribes(:user_id)');
        $req->bindValue(':user_id',$_COOKIE['USER_ID']);
        $req->execute();
        while ($obj = $req->fetch(\PDO::FETCH_ASSOC)){
            $obj['findall_subscribes'] = trim($obj['findall_subscribes'], '()');
            $ar = explode(",",$obj['findall_subscribes']);
    ?>
    <div class="item">
            <div class='info'>
            <p>Тип: <?php echo $ar[0];?></p>
            <p>Дата оформления: <?php echo date("d.m.Y", strtotime($ar[1]));?></p>
            <p>Дата окончания: <?php echo date("d.m.Y", strtotime($ar[2]));?></p>
            <p>Цена: <?php echo $ar[3];?>р</p>
        </div>
    </div>
    <?php
        }
    ?>
    </div>
</div>
<?php include_once "footer.php";?>
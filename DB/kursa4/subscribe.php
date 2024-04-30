<?php 
include_once "header.php";
include_once "db.php";
global $db_connect;
?>
<div class='main'>
    <h1>Подписка</h1>
    <div class="btn_block">
        <button class="btn" onclick="showForm(this)">Оформить новую подписку</button>
        <form action="" id='form' class="subscribe-form">
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
                    <option value="Посудомойка">Посудомойка</option>
                    <option value="Холодильник">Холодильник</option>
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
            <p>Дата списания: <?php echo date("d.m.Y", strtotime($ar[2]));?></p>
            <p>Цена: <?php echo $ar[3];?>р</p>
        </div>
    </div>
    <?php
        }
    ?>
        <!-- <div class="item">
            <div class='info'>
                <p>Тип: кухонная техника</p>
                <p>Дата оформления: 10.11.2023</p>
                <p>Дата списания: 20.12.2023</p>
                <p>Цена: 300р</p>
            </div>
        </div> -->
    </div>
</div>
<?php include_once "footer.php";?>
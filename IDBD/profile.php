<?php include_once "header.php";?>
<div class='main'>
<h1>Профиль</h1>
<?php if ($_GET['change'] == 'y'):?>
    <form method='POST' class='schedule-form' id='form' style="display: flex;" action="backend.php">
    <input type="text" hidden name="action_type" value="schedule">
            <div class="element">
                <label for="days[]">Выберите дни</label>
                <select  name="days[]" required multiple="multiple">
                    <option value="1">Понедельник</option>
                    <option value="2">Вторник</option>
                    <option value="3">Среда</option>
                    <option value="4">Четверг</option>
                    <option value="5">Пятница</option>
                    <option value="6">Суббота</option>
                    <option value="7">Воскресенье</option>
                </select>
            </div>
            <input type="submit" class='btn' value="Сохранить">
        </form>
<?php else:?>
    <div class="list">
        <div class="item">
            <div class='photo'>
                <img src="/~s338923/isbd/img/worker-<?=$_COOKIE['USER_ID']?>.jpg" alt="master_photo" class="avatar">
            </div>
            <?php
            $req = $db_connect->prepare('SELECT get_master_profile(:user_id)');
            $req->bindValue(':user_id',$_COOKIE['USER_ID']);
            $req->execute();
            $obj = $req->fetch(\PDO::FETCH_ASSOC);
            $obj['get_master_profile'] = trim($obj['get_master_profile'], '()');
            $ar = explode(",",$obj['get_master_profile']);
        ?>
            <div class='info'>
                <p><?=$ar[0]?> <?=$ar[1]?></p>
                <p>Рейтинг: <?=$ar[2]?></p>
                <p>Стаж: <?=$ar[4]?></p>
                <p>Расписание:
                <?php
            $req = $db_connect->prepare('SELECT get_master_schedule(:user_id)');
            $req->bindValue(':user_id',$_COOKIE['USER_ID']);
            $req->execute();
            while($obj = $req->fetch(\PDO::FETCH_ASSOC)) {
            $obj['get_master_schedule'] = trim($obj['get_master_schedule'], '()');
            $ar = explode(",",$obj['get_master_schedule']);?>
                <?= $ar[1]?> 
            <?php }?>
        </p>
                <a href="/~s338923/isbd/profile.php?change=y">Изменить расписание</a>
            </div>
        </div>
    </div>
<?php endif?>
</div>
<?php include_once "footer.php";?>
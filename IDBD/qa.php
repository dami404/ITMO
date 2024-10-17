<?php 
include_once "header.php";
include_once "db.php";
global $db_connect;
?>
<div class='main'>
    <h1>Вопросы и ответы</h1>
    <div class="btn_block">
        <button class="btn" onclick="showForm(this)">Новый вопрос</button>
        <form action="backend.php" id='form' class="qa-form" method="post">
        <input type="text" hidden name="action_type" value="qa">
            <div class="element">
                <label for="theme">Тема</label>
                <input type="text" name='theme' required>
            </div>
            <div class="element">
                <label for="comment">Напишите вопрос</label>
                <input type="text" name='comment' required>
            </div>
            <input type="submit" class='btn' value="Сохранить">
        </form>
    </div>
    <div class="list">

    <?php
        $req = $db_connect->query('SELECT findall_faq()');
        while ($obj = $req->fetch(\PDO::FETCH_ASSOC)){
            $obj['findall_faq'] = trim($obj['findall_faq'], '()');
            $ar = explode('","',$obj['findall_faq']);
    ?>
    <div class="item">
            <div class='info'>
            <p><?php echo $ar[0];?></p>
            <p><?php echo $ar[1];?></p>
        </div>
    </div>
    <?php
        }
    ?>

            <!-- <div class="item">
            <div class='info'>
                <p>Как вызвать мастера на дом?</p>
                <p>Вы можете вызвать мастера на вкладке "Заказы", указав необходимую информацию о вашей технике и проблеме.</p>
            </div>
        </div>
        <div class="item">
            <div class='info'>
                <p>Как вызвать мастера на дом?</p>
                <p>Вы можете вызвать мастера на вкладке "Заказы", указав необходимую информацию о вашей технике и проблеме.</p>
            </div>
        </div>
        <div class="item">
            <div class='info'>
                <p>Как вызвать мастера на дом?</p>
                <p>Вы можете вызвать мастера на вкладке "Заказы", указав необходимую информацию о вашей технике и проблеме.</p>
            </div>
        </div>
        <div class="item">
            <div class='info'>
                <p>Как вызвать мастера на дом?</p>
                <p>Вы можете вызвать мастера на вкладке "Заказы", указав необходимую информацию о вашей технике и проблеме.</p>
            </div>
        </div> -->
    </div>
</div>
<?php include_once "footer.php";?>
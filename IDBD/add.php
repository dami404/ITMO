<?php include_once "header.php";?>
<div class='main'>
    <h1>Добавление прибора</h1>
    <form method='POST' class='rate-order-form' id='form' style="display: flex;" action="backend.php">
    <input type="text" hidden name="action_type" value="add_technique">
    <?php $stmt = $db_connect->query('SELECT get_technique()');?>
            <div class="element">
                <label for="date">Дата покупки</label>
                <input type="date" name='date' required>
            </div>
            <div class="element">
                <label for="technique">Выберите прибор</label>
                <select  name="technique" required>
                <?php while ($obj = $stmt->fetch(\PDO::FETCH_ASSOC)) {
                    $obj['get_technique'] = trim($obj['get_technique'], '()');
                    $ar = explode(",",$obj['get_technique']);
                    ?>
                    <option value="<?=$ar[0]?>"><?=$ar[1]?></option>
                <?php }?>
                </select>
            </div>
            <input type="submit" class='btn' value="Сохранить">
        </form>
    <h2>Ваши приборы</h2>
    <div class="list">
        <?php
        $stmt = $db_connect->prepare('SELECT get_owners(:user)');

        $stmt->bindValue(':user', $_COOKIE['USER_ID']);
        // execute the statement
        $stmt->execute();

        // return the result set as an object
        while ($obj = $stmt->fetch(\PDO::FETCH_ASSOC)) {
            $obj['get_owners'] = trim($obj['get_owners'], '()');
            $ar = explode(",",$obj['get_owners']);
            // var_dump($ar);
            ?>
            <div class="item">
                <div class='info'>
                    <p>Название: <?= $ar[2]?></p>
                    <p>Дата покупки: <?= $ar[1]?></p>
                </div>
            </div>
        <?php }?>
    </div>
</div>
<?php include_once "footer.php";?>
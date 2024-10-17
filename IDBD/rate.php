<?php include_once "header.php";?>
<div class='main'>
    <h1>Оценка заказа</h1>
    <form method='POST' class='rate-order-form' id='form' style="display: flex;" action="backend.php">
    <input type="text" hidden name="action_type" value="rate_order">
    <input type="text" hidden name="order_id" value="<?=$_GET["id"]?>">
            <div class="element">
                <label for="date">Рейтинг</label>
                <input type="number" name='rating' required min="0" max="5" step="0.1">
            </div>
            <div class="element">
                <label for="comment">Комментарий</label>
                <input type="text" name='comment' required>
            </div>
            <input type="submit" class='btn' value="Сохранить">
        </form>
</div>
<?php include_once "footer.php";?>

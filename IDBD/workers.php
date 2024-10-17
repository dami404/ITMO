<?php 
include_once "header.php";
include_once "db.php";
global $db_connect;
?>

<div class='main'>
<h1>Мастера</h1>
    <div class="list">
        <?php
         try {
            $req = $db_connect->query('SELECT findall_workers()');
            while ($obj = $req->fetch(\PDO::FETCH_ASSOC)){
                $obj['findall_workers'] = trim($obj['findall_workers'], '()');
                $ar = explode(",",$obj['findall_workers']);

                $req2=$db_connect->prepare('SELECT get_master_schedule(:master_id)');
                $req2->bindValue(':master_id',$ar[4]);
                $req2->execute();
                $full_schedule='';

               while( $obj2 = $req2->fetch(\PDO::FETCH_ASSOC)){
                $obj2['get_master_schedule']=trim($obj2['get_master_schedule'], '()');
                $schedule = explode(",",$obj2['get_master_schedule']);
                $full_schedule.=$schedule[1].' ';

               }
        ?>
        <div class="item">
                <div class='info'>
                    <div class='photo'>
                        <img src="/~s338923/isbd/img/worker-<?php echo $ar[4]?>.jpg" alt="master_photo" class="avatar">
                    </div>
                <p><?php echo $ar[1];?></p>
                <p>Рейтинг: <?php echo $ar[2];?></p>
                <p>Стаж(год): <?php echo $ar[3];?></p>
                <p>Расписание: <?php echo $full_schedule;?></p>
                <a href="/~s338923/isbd/index.php?masterId=<?php echo $ar[4]?>">Создать заказ</a>
            </div>
        </div>
        <?php
            }
        }catch(Error $e){
            echo $e;
        }
        ?>
    </div>
</div>
<?php include_once "footer.php";?>
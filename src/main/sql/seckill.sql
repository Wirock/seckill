--��ɱִ�д洢����(qps���Ϊ6000)
DELIMITER $$ -- console;ת��Ϊ$$
--����洢����
--row_count()������һ���޸�����sql(delete,insert,update)��Ӱ������
--row_count: 0��δ�޸ģ�>0���޸ĵ�������<0��sql����δִ�� 
CREATE PROCEDURE `seckill`.`execute_seckill`
(in v_seckill_id bigint,in v_phone bigint,
in v_kill_time timestamp,out r_result int)
BEGIN
		DECLARE insert_count int DEFAULT 0;
		DECLARE num int DEFAULT 0;
		DECLARE seckill_start timestamp DEFAULT '2016-01-01';
		DECLARE seckill_end timestamp DEFAULT '2016-01-01';
		SELECT start_time,end_time,number FROM seckill 
			WHERE seckill_id = v_seckill_id
			INTO seckill_start,seckill_end,num;
		START TRANSACTION;
		IF(seckill_end > v_kill_time 
				AND seckill_start < v_kill_time 
				AND num>0) THEN
			INSERT IGNORE INTO success_killed(seckill_id,phone,create_time,state)
				VALUES(v_seckill_id,v_phone,v_kill_time,0);
			SELECT row_count() INTO insert_count;
		END IF;
		IF(insert_count = 0) THEN
			ROLLBACK;
			SET r_result = -1;
		ELSEIF(insert_count < 0) THEN
			ROLLBACK;
			SET r_result = -2;
		ELSE
			UPDATE seckill SET number = number - 1 
				WHERE seckill_id = v_seckill_id
					AND end_time > v_kill_time
					AND start_time < v_kill_time
					AND number > 0;
			IF(insert_count = 0) THEN
				ROLLBACK;
				SET r_result = 0;
			ELSEIF(insert_count < 0) THEN
				ROLLBACK;
				SET r_result = -2;
			ELSE
				COMMIT;
				SET r_result = 1;
			END IF;
		END IF; 
END;
$$
DELIMITER ;

--����һ������
SET @r_result = -3;
--ִ�д洢����
CALL execute_seckill(1003,13502178831,now(),@r_result);
-- ��ȡ���
SELECT @r_result;

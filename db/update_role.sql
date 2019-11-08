DROP PROCEDURE IF EXISTS UPDATE_ROLE;
DELIMITER //
CREATE PROCEDURE UPDATE_ROLE(
IN P_ROLE_ID INT
,IN P_ROLE_NAME VARCHAR(20)
,IN P_ROLE_STATE INT
,IN P_ROLE_TYPE_ID INT
,IN P_REMARK VARCHAR(120)
,IN P_MENU_IDS VARCHAR(100)
,OUT ERROR_NO INT
)
BEGIN
DECLARE IDX INT;
DECLARE P_MENU_ID INT;
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET ERROR_NO=0;
SET ERROR_NO=1;

START TRANSACTION;

UPDATE pss_user_role_info
SET
role_name = P_ROLE_NAME
,role_state = P_ROLE_STATE
,role_type_id = P_ROLE_TYPE_ID
,remarks = P_REMARK
,upd_date = NOW()
WHERE
role_id = P_ROLE_ID;

SET IDX = LOCATE(',',P_MENU_IDS);
IF IDX =0 AND LENGTH(P_MENU_IDS)>0 THEN
  SET IDX = LENGTH(P_MENU_IDS)+1;
END IF;

DELETE FROM pss_role_perm_info WHERE role_id = P_ROLE_ID;


WHILE IDX>0
DO
  SET P_MENU_ID = LEFT(P_MENU_IDS,IDX-1);
	INSERT INTO pss_role_perm_info(
	role_id
	,menu_id
	,cre_date
	,upd_date
	)
	VALUES
	(
	P_ROLE_ID
	,P_MENU_ID
	,NOW()
	,NOW()
	);

	SET P_MENU_IDS = SUBSTR(P_MENU_IDS FROM IDX+1);
	SET IDX = LOCATE(',',P_MENU_IDS);
	IF IDX = 0 AND LENGTH(P_MENU_IDS)>0 THEN
	   SET IDX = LENGTH(P_MENU_IDS)+1;
	END IF;
END WHILE;

IF ERROR_NO = 0 THEN
  ROLLBACK;
ELSE
   COMMIT;
END IF;

END //

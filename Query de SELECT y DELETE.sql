  SELECT * FROM cgssa_sandbox.dbo.TEST

  SELECT COUNT(*) FROM dbo.TEST;

 -- SELECT * FROM dbo.TEST where stock_code = '000747562';
 
 -- DELETE FROM dbo.TEST
 -- truncate table dbo.TEST

 SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('dbo.TEST') 
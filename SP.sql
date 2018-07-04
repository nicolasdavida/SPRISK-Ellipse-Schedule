IF (OBJECT_ID('sp_respaldo') IS NOT NULL)
DROP PROCEDURE dbo.sp_respaldo

CREATE PROCEDURE dbo.sp_respaldo
	@idEntrada varchar(255)
	as
	Declare @id as varchar(255)
	Declare @costo decimal(10, 2)
	Declare @stock int
	Declare @leadtime decimal(10, 0)
	Declare @contador_reg int

	DECLARE cursor_rmes CURSOR FOR 
	SELECT stock_code, ISNULL(invent_cost_pr, 0), stock_total_act, lead_time FROM dbo.tsrmes_rpto_oracle
	 
	OPEN cursor_rmes
	FETCH NEXT FROM cursor_rmes into @id,@costo,@stock,@leadtime
	 
	WHILE @@FETCH_STATUS=0
	BEGIN
	UPDATE dbo.tsrmes_repto SET costo = @costo, cant_stock = @stock, lead_time_comp = @leadtime WHERE ds_co = @id
	FETCH NEXT FROM cursor_rmes into @id,@costo,@stock,@leadtime 
	END
	close cursor_rmes
	Deallocate cursor_rmes
GO

EXEC dbo.sp_respaldo 0

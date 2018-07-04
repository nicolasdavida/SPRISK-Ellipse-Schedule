IF (OBJECT_ID('sp_respaldo') IS NOT NULL)
DROP PROCEDURE dbo.sp_respaldo

CREATE PROCEDURE dbo.sp_respaldo
	@idEntrada varchar(255)
	as
	Declare @id as varchar(32)
	Declare @costo decimal(10, 2)
	Declare @stock int
	Declare @contador_reg int

	DECLARE cursor_rmes CURSOR FOR 
	SELECT stock_code, invent_cost_pr, stock_total_act FROM dbo.tsrmes_rpto_oracle
	 
	OPEN cursor_rmes
	FETCH NEXT FROM cursor_rmes into @id,@costo,@stock
	 
	WHILE @@FETCH_STATUS=0
	BEGIN
	update dbo.tsrmes_repto SET costo = @costo, cant_stock = @stock WHERE ds_co = @id
	FETCH NEXT FROM cursor_rmes into @id,@costo,@stock 
	END
	close cursor_rmes
	Deallocate cursor_rmes
GO

EXEC dbo.sp_respaldo 0

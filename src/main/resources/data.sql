insert into riesgo_entity (id, identificador_imueble, valor_canon_riesgo, estado)
values
  ('r-001', 'APT-101', 850000, 'ACTIVO'),
  ('r-002', 'CASA-202', 1250000, 'CANCELADO'),
  ('r-003', 'BODEGA-303', 980000, 'ACTIVO'),
  ('r-004', 'BODEGA-302', 1080000, 'ACTIVO');

insert into poliza_entity (id, numero_poliza, fecha_inicio, fecha_fin, valor_canon_mensual, valor_prima, estado)
values
  ('p-001', 'POL-001', timestamp '2026-05-01 00:00:00', timestamp '2027-05-01 00:00:00', 120000, 1440000, 'ACTIVA'),
  ('p-002', 'POL-002', timestamp '2026-05-10 00:00:00', timestamp '2027-05-10 00:00:00', 180000, 2160000, 'PENDIENTE');

insert into poliza_individual_entity (id, riesgo_id)
values ('p-001', 'r-001');

insert into poliza_colectiva_entity (id)
values ('p-002');

update riesgo_entity
set poliza_id = 'p-002'
where id in ('r-002', 'r-003');
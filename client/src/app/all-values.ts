import { Tables, Sources } from './value';

export const SOURCES: Sources[] = [
  {source: "default", tables: ['medicare_demographic', 'db2', 'db3']},
  {source: "source2", tables: ['db1', 'db2', 'db3']},
  {source: "source3", tables: ['db1', 'db2', 'db3']},
  {source: "source4", tables: ['db1', 'db2', 'db3']},
];

export const TABLES: Tables[] = [
  {table: "medicare_demographic", columns: ['name', 'id', 'county', 'number']},
  {table: 'db1', columns : ['col1', 'col2', 'col3']},
  {table: 'db2', columns : ['col1', 'col2', 'col3']},
  {table: 'db3', columns : ['col1', 'col2', 'col3']},
]

export const MAX_COLUMNS: number = 4;
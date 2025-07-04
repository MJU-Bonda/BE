ALTER TABLE book
    MODIFY COLUMN subject ENUM(
    'ALL',
    'ART',
    'BURNOUT',
    'COFFEE',
    'COMFORT',
    'COOKING',
    'LOVE',
    'MOVIE',
    'MUSIC',
    'PET',
    'PLANT',
    'SELF_DEVELOPMENT',
    'SPACE'
    ) NULL;
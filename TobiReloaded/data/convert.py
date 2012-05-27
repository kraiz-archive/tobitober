import json, os, re


def cut_out(map_i, after, until=None):
    """Cut out the file content between the lines matching the regexps <after> and <until>"""
    f = open('map_%d.bb' % map_i, 'r')
    start = False
    for line in f.readlines():
        if start and until is not None and re.match(until, line):
            break
        if start:
            yield line
        if re.match(after, line):
            start = True
    f.close()

map_geo = {
    # lvl: width
    1: 160,
    2: 121,
    3: 50,
    4: 100
}

monster_types = {
    1: 'water',
    2: 'schnappi',
    3: 'kroko',
    4: 'ghost',
    5: 'nsa',
    6: 'robot',
    7: 'zwiebli',
    8: 'zwiablo'
}

out = open('maps.js', 'w')
out.write('var MAPS = %s;' % json.dumps({
    map_i: {
        'width': map_geo[map_i],
        'tiles': [
            int(tile)
            for line in cut_out(map_i, r'^\.level_texMapData%d$' % map_i, r'^\.zwiebeldata%d$' % map_i)
            for tile in re.findall(r'(\d{1,2})-\d', line)
        ],
        'onions': [
            (int(x), int(y))
            for line in cut_out(map_i, r'^\.zwiebeldata%d$' % map_i, r'^.monsterdata%d$' % map_i)
            for x, y in re.findall(r'^data (\d{1,3}), (\d{1,3})$', line)
        ],
        'monsters': [
            (int(x), int(y), monster_types[int(type)], int(direction))
            for line in cut_out(map_i, r'^\.monsterdata%d$' % map_i)
            for x, y, type, direction in re.findall(r'data (\d{1,3}), (\d{1,3}), (\d), (\d)', line)
        ]
    } for map_i in range(1,5)})
)
out.close()
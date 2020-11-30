#ifndef MAP_H
#define MAP_H

#include <set>
#include <string>
#include <map>
#include <vector>
#include <iostream>

enum TileType {standard, hindering, blocking};

class MapTile
{
private:
    std::set<MapTile*> m_adjacentTiles;
    std::string m_name;
    //TileType m_tileType;
public:
    MapTile(std::string name);
    void addAdjacentTile(MapTile* tile);
    std::set<MapTile*> getAdjacentTiles();
    std::string getName();
    bool isAdjacentToTile(MapTile* tile);
};

class Map
{
private:
    std::map<std::string, MapTile*> m_tilesByName;
public:
    Map();
    void addTile(MapTile* tile);
    MapTile* getTileByName(std::string name);
    std::set<MapTile*> getReachableTilesFromTile(MapTile* tile, int distance);
    std::set<MapTile*> getReachableTilesFromTile(std::string tileName, int distance);
};

#endif
#include <iostream>
#include "Map.h"

int main(int argc, char *argv[])
{
    Map m;

    for (auto tile : m.getReachableTilesFromTile(m.getTileByName("a1"), 1))
    {
        std::cout << tile->getName() << std::endl;
    }
    return 0;
}
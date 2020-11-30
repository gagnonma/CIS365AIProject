#ifndef HEROS_H
#define HEROS_H

class Hero
{
private:
    int clickIndex = 0;
    mapNode* position;
public:
    Hero();
    void click(int numClicks);
}
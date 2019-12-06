class team:
    usedlist = [0 for i in range(1000)]
    teamlist = [0 for i in range(1000)]

    def __init__(self):
        for i in range(1000):
            self.usedlist[i] = 0
            self.teamlist[i] = 0

    def add(self,aid):
        idx = -1
        for i in range(0,1000):
            if self.usedlist[i] == 0:
                idx = i
                self.usedlist[i] = 1
                break
         
        self.teamlist[idx] = aid
        return idx
        
    def delt(self,aid):
        if aid in self.teamlist:
            idx = self.teamlist.index(aid)
            self.usedlist[idx] = 0
            return True
        else :
            return False

    def check(self,indx):
        if self.usedlist[indx] == 1:
            return self.teamlist[indx]
        else:
            return -1

    def show(self):
        for i in self.teamlist:
            if not i == 0:
                print(i)
QUEUE = team()
public interface Queue<AnyType>
{
int size();
boolean isEmpty();
void enqueue(AnyType e);
AnyType first();
AnyType dequeue();
}
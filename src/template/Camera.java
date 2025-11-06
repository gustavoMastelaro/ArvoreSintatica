package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.math.Vector2;

public class Camera {
   
    public Vector2 pos;
    private Vector2 dim;
    private Vector2 vel;
    private double speed;

    public Camera( Vector2 pos, Vector2 dim, double speed ) {
        this.pos = pos;
        this.dim = dim;
        this.vel = new Vector2();
        this.speed = speed;
    }
    
    public void update( double delta, Rectangle boundary, EngineFrame engine ) {
        
        if ( engine.isKeyDown(EngineFrame.KEY_LEFT ) ) {
            vel.x = -speed;
        } else if ( engine.isKeyDown(EngineFrame.KEY_RIGHT ) ) {
            vel.x = speed;
        } else {
            vel.x = 0;
        }
        
        if ( engine.isKeyDown(EngineFrame.KEY_UP ) ) {
            vel.y = -speed;
        } else if ( engine.isKeyDown(EngineFrame.KEY_DOWN ) ) {
            vel.y = speed;
        } else {
            vel.y = 0;
        }
        
        pos.x += vel.x * delta;
        pos.y += vel.y * delta;
        
        if ( pos.x - dim.x / 2 <= boundary.x ) {
            pos.x = boundary.x + dim.x / 2;
        } else if ( pos.x + dim.x / 2 >= boundary.x + boundary.width ) {
            pos.x = boundary.x + boundary.width - dim.x / 2;
        }
        
        if ( pos.y - dim.y / 2 <= boundary.y ) {
            pos.y = boundary.y + dim.y / 2;
        } else if ( pos.y + dim.y / 2 >= boundary.y + boundary.height ) {
            pos.y = boundary.y + boundary.height - dim.y / 2;
        }
        
    }
       
}
